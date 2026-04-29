// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.toolwindow

import com.digitalsubstrate.dsm.services.DSMValidationService
import com.intellij.icons.AllIcons
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.table.AbstractTableModel

/**
 * Panel displaying DSM validation results.
 */
class DSMValidationPanel(private val project: Project) : JPanel(BorderLayout()) {

    private val tableModel = ValidationTableModel()
    private val table = JBTable(tableModel)
    private val statusLabel = JBLabel("No validation run yet")
    private val validateButton = JButton("Validate", AllIcons.Actions.Execute)
    private val prevButton = JButton("Previous Error", AllIcons.Actions.Back)
    private val nextButton = JButton("Next Error", AllIcons.Actions.Forward)

    private val crashTextArea = javax.swing.JTextArea().apply {
        isEditable = false
        lineWrap = false
        font = com.intellij.util.ui.JBFont.create(java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 12))
    }
    private val centerLayout = java.awt.CardLayout()
    private val centerPanel = JPanel(centerLayout)
    companion object {
        private const val CARD_TABLE = "table"
        private const val CARD_CRASH = "crash"
    }

    init {
        // Setup table
        table.autoCreateRowSorter = true
        table.setShowGrid(true)

        // Column widths
        table.columnModel.getColumn(0).preferredWidth = 300 // File
        table.columnModel.getColumn(1).preferredWidth = 40  // Line
        table.columnModel.getColumn(2).preferredWidth = 40  // Column
        table.columnModel.getColumn(3).preferredWidth = 500 // Message

        // Double-click to navigate to error
        table.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {
                    val row = table.rowAtPoint(e.point)
                    if (row >= 0) {
                        navigateToError(row)
                    }
                }
            }
        })

        // Setup validate button — call the service directly (project is already in scope).
        validateButton.addActionListener {
            // saveAllDocuments() requires write-intent access; a Swing action listener
            // doesn't hold any lock, so wrap explicitly. WriteAction is the stable
            // public API; WriteIntentReadAction is still @ApiStatus.Experimental in 2025.1.
            com.intellij.openapi.application.WriteAction.run<RuntimeException> {
                com.intellij.openapi.fileEditor.FileDocumentManager.getInstance().saveAllDocuments()
            }
            val target = resolveValidationTarget()
            if (target == null) {
                DSMValidationToolWindowFactory.showError(project, "No selection, no open file, and project has no base path")
                return@addActionListener
            }
            DSMValidationService.getInstance(project).validateDirectory(target, showToolWindow = true)
        }
        validateButton.toolTipText = "Run DSM validation (Ctrl+Alt+V)"

        // Setup navigation buttons
        prevButton.addActionListener { navigateToPreviousError() }
        nextButton.addActionListener { navigateToNextError() }
        prevButton.toolTipText = "Navigate to previous error (Alt+Shift+F2)"
        nextButton.toolTipText = "Navigate to next error (Alt+F2)"

        // Toolbar with validate and navigation buttons
        val toolbar = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            add(validateButton)
            add(Box.createHorizontalStrut(10))
            add(prevButton)
            add(Box.createHorizontalStrut(5))
            add(nextButton)
            add(Box.createHorizontalGlue())
        }

        // Layout — center is a CardLayout that swaps between the diagnostics table
        // and a multi-line text area used to display crash output from dsm_util.py.
        centerPanel.add(JBScrollPane(table), CARD_TABLE)
        centerPanel.add(JBScrollPane(crashTextArea), CARD_CRASH)
        centerLayout.show(centerPanel, CARD_TABLE)

        add(toolbar, BorderLayout.NORTH)
        add(centerPanel, BorderLayout.CENTER)
        add(statusLabel, BorderLayout.SOUTH)
    }

    /**
     * Resolve the directory to validate. Always returns a directory — dsm_util.py
     * needs the full set of sibling files to resolve cross-references. Priority:
     *   1. Current selection in the Project view (folder, or file's parent folder)
     *   2. Parent directory of the file currently open in the editor
     *   3. project.basePath
     */
    private fun resolveValidationTarget(): String? {
        // 1. Project view selection
        val projectViewPane = com.intellij.ide.projectView.ProjectView.getInstance(project).currentProjectViewPane
        val tree = projectViewPane?.tree
        if (tree != null) {
            val ctx = com.intellij.ide.DataManager.getInstance().getDataContext(tree)
            val selected = ctx.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
            if (selected != null) {
                return if (selected.isDirectory) selected.path else selected.parent?.path
            }
        }
        // 2. Editor
        com.intellij.openapi.fileEditor.FileEditorManager.getInstance(project)
            .selectedFiles.firstOrNull()?.parent?.path?.let { return it }
        // 3. Fallback
        return project.basePath
    }

    /**
     * Update the panel with new validation results.
     */
    fun updateResults(result: DSMValidationService.ValidationResult) {
        tableModel.setErrors(result.errors)

        // Crash case: ERROR status with no parsable diagnostics — show raw output.
        val isCrash = result.status == DSMValidationService.ValidationResult.Status.ERROR &&
            result.errors.isEmpty() &&
            !result.message.isNullOrBlank()
        if (isCrash) {
            crashTextArea.text = result.message
            crashTextArea.caretPosition = 0
            centerLayout.show(centerPanel, CARD_CRASH)
        } else {
            crashTextArea.text = ""
            centerLayout.show(centerPanel, CARD_TABLE)
        }

        statusLabel.text = when (result.status) {
            DSMValidationService.ValidationResult.Status.SUCCESS -> "✓ ${result.message}"
            DSMValidationService.ValidationResult.Status.ERROR ->
                if (isCrash) "✗ dsm_util.py crashed — see output above"
                else "✗ ${result.message}"
            DSMValidationService.ValidationResult.Status.CANCELLED -> "⚠ ${result.message}"
        }
    }

    /**
     * Navigate to the file and line of the selected error.
     */
    private fun navigateToError(row: Int) {
        val modelRow = table.convertRowIndexToModel(row)
        val error = tableModel.getError(modelRow) ?: return

        val virtualFile = LocalFileSystem.getInstance().findFileByPath(error.filePath)
        if (virtualFile != null) {
            val descriptor = OpenFileDescriptor(project, virtualFile, error.line - 1, error.column - 1)
            descriptor.navigate(true)
        }
    }

    /**
     * Navigate to the next error in the list (cycles to first if at end).
     * Can be called from actions (Alt+F2).
     */
    fun navigateToNextError() {
        if (tableModel.rowCount == 0) return

        val currentRow = table.selectedRow
        val nextRow = if (currentRow < 0 || currentRow >= table.rowCount - 1) {
            0  // Cycle to first
        } else {
            currentRow + 1
        }

        table.setRowSelectionInterval(nextRow, nextRow)
        table.scrollRectToVisible(table.getCellRect(nextRow, 0, true))
        navigateToError(nextRow)
    }

    /**
     * Navigate to the previous error in the list (cycles to last if at start).
     * Can be called from actions (Alt+Shift+F2).
     */
    fun navigateToPreviousError() {
        if (tableModel.rowCount == 0) return

        val currentRow = table.selectedRow
        val prevRow = if (currentRow <= 0) {
            table.rowCount - 1  // Cycle to last
        } else {
            currentRow - 1
        }

        table.setRowSelectionInterval(prevRow, prevRow)
        table.scrollRectToVisible(table.getCellRect(prevRow, 0, true))
        navigateToError(prevRow)
    }

    /**
     * Table model for validation errors.
     */
    private class ValidationTableModel : AbstractTableModel() {
        private val columns = arrayOf("File", "Line", "Column", "Message")
        private val errors = mutableListOf<DSMValidationService.ValidationError>()

        override fun getRowCount(): Int = errors.size
        override fun getColumnCount(): Int = columns.size
        override fun getColumnName(column: Int): String = columns[column]

        override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
            val error = errors[rowIndex]
            return when (columnIndex) {
                0 -> File(error.filePath).name  // Just filename, not full path
                1 -> error.line
                2 -> error.column
                3 -> error.message
                else -> ""
            }
        }

        fun setErrors(newErrors: List<DSMValidationService.ValidationError>) {
            errors.clear()
            errors.addAll(newErrors)
            fireTableDataChanged()
        }

        fun getError(row: Int): DSMValidationService.ValidationError? {
            return errors.getOrNull(row)
        }
    }
}
