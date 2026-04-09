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

        // Setup validate button
        validateButton.addActionListener {
            // Trigger validation action
            val action = com.intellij.openapi.actionSystem.ActionManager.getInstance()
                .getAction("DSM.ValidateFiles")
            action?.let {
                val dataContext = com.intellij.openapi.actionSystem.DataContext { dataId ->
                    when (dataId) {
                        com.intellij.openapi.actionSystem.CommonDataKeys.PROJECT.name -> project
                        else -> null
                    }
                }
                val event = com.intellij.openapi.actionSystem.AnActionEvent.createFromAnAction(
                    it,
                    null,
                    com.intellij.openapi.actionSystem.ActionPlaces.UNKNOWN,
                    dataContext
                )
                it.actionPerformed(event)
            }
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

        // Layout
        add(toolbar, BorderLayout.NORTH)
        add(JBScrollPane(table), BorderLayout.CENTER)
        add(statusLabel, BorderLayout.SOUTH)
    }

    /**
     * Update the panel with new validation results.
     */
    fun updateResults(result: DSMValidationService.ValidationResult) {
        tableModel.setErrors(result.errors)

        statusLabel.text = when (result.status) {
            DSMValidationService.ValidationResult.Status.SUCCESS -> "✓ ${result.message}"
            DSMValidationService.ValidationResult.Status.ERROR -> "✗ ${result.message}"
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
