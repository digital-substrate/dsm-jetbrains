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
                toolWindow?.show {
                    val panel = project.getUserData(VALIDATION_PANEL_KEY)
                    panel?.updateResults(result)
                }
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
