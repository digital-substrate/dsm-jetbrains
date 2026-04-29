// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.toolwindow

import com.digitalsubstrate.dsm.services.DSMValidationService
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory

/**
 * Factory for creating the DSM Validation tool window.
 */
class DSMValidationToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val validationPanel = DSMValidationPanel(project)
        val content = ContentFactory.getInstance().createContent(validationPanel, "", false)
        toolWindow.contentManager.addContent(content)

        // Store panel reference for updating results
        project.putUserData(VALIDATION_PANEL_KEY, validationPanel)
    }

    companion object {
        internal val VALIDATION_PANEL_KEY = com.intellij.openapi.util.Key.create<DSMValidationPanel>("DSM_VALIDATION_PANEL")

        /**
         * Show validation results in the tool window.
         * Must be called from EDT or will be automatically invoked on EDT.
         */
        fun showResults(project: Project, result: DSMValidationService.ValidationResult) {
            ApplicationManager.getApplication().invokeLater {
                val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("DSM Validation")
                toolWindow ?: return@invokeLater
                val panel = project.getUserData(VALIDATION_PANEL_KEY)
                panel?.updateResults(result)
                updateToolWindowBadge(toolWindow, result)
                toolWindow.show()
            }
        }

        private fun updateToolWindowBadge(
            toolWindow: ToolWindow,
            result: DSMValidationService.ValidationResult
        ) {
            val (icon, tabName) = when (result.status) {
                DSMValidationService.ValidationResult.Status.SUCCESS ->
                    com.intellij.icons.AllIcons.General.InspectionsOK to "DSM Validation"
                DSMValidationService.ValidationResult.Status.ERROR -> {
                    val count = result.errors.size
                    if (count > 0) {
                        com.intellij.icons.AllIcons.General.InspectionsError to "DSM Validation ($count)"
                    } else {
                        com.intellij.icons.AllIcons.General.BalloonError to "DSM Validation (!)"
                    }
                }
                DSMValidationService.ValidationResult.Status.CANCELLED ->
                    com.intellij.icons.AllIcons.General.InspectionsOK to "DSM Validation"
            }
            toolWindow.setIcon(icon)
            toolWindow.contentManager.contents.firstOrNull()?.displayName = tabName
            // Bring attention to the stripe button on errors.
            if (result.status == DSMValidationService.ValidationResult.Status.ERROR) {
                toolWindow.setAvailable(true)
            }
        }

        /**
         * Show error message in the tool window.
         */
        fun showError(project: Project, message: String) {
            showResults(project, DSMValidationService.ValidationResult.error(message))
        }
    }
}
