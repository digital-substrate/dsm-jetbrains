package com.digitalsubstrate.dsm.actions

import com.digitalsubstrate.dsm.services.DSMValidationService
import com.digitalsubstrate.dsm.toolwindow.DSMValidationToolWindowFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager

/**
 * Action to validate DSM files using dsm_util.py.
 * Displays results in the DSM Validation tool window.
 */
class DSMValidateAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        // Save all unsaved documents before validation
        // (dsm_util.py reads from disk, not from memory)
        FileDocumentManager.getInstance().saveAllDocuments()

        // Get directory to validate
        val directory = getDirectoryToValidate(e)
        if (directory == null) {
            DSMValidationToolWindowFactory.showError(project, "No DSM files or directory selected")
            return
        }

        // Use the validation service (shows tool window)
        val validationService = DSMValidationService.getInstance(project)
        validationService.validateDirectory(directory, showToolWindow = true)
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }

    /**
     * Get directory to validate from action context.
     * Priority: selected directory > current file's directory > project base directory
     */
    private fun getDirectoryToValidate(e: AnActionEvent): String? {
        val project = e.project ?: return null

        // Try selected file/directory
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (virtualFile != null) {
            return if (virtualFile.isDirectory) {
                virtualFile.path
            } else {
                virtualFile.parent?.path
            }
        }

        // Fallback to project base path
        return project.basePath
    }
}
