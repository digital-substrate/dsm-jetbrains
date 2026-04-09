package com.digitalsubstrate.dsm.actions

import com.digitalsubstrate.dsm.toolwindow.DSMValidationToolWindowFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

/**
 * Navigate to previous DSM validation error (Alt+Shift+F2).
 * Cycles to last error if at the start of the list.
 */
class DSMPreviousErrorAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val panel = getValidationPanel(project) ?: return
        panel.navigateToPreviousError()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.project != null && getValidationPanel(e.project!!) != null
    }

    private fun getValidationPanel(project: Project) =
        project.getUserData(DSMValidationToolWindowFactory.VALIDATION_PANEL_KEY)
}
