package com.digitalsubstrate.dsm.actions

import com.digitalsubstrate.dsm.core.DSMIcons
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import java.util.*

class DSMCreateFileAction : CreateFileFromTemplateAction(
    "DSM File",
    "Create a new DSM file",
    DSMIcons.FILE
), DumbAware {

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder
            .setTitle("New DSM File")
            .addKind("DSM File", DSMIcons.FILE, "DSM File")
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return "Create DSM File $newName"
    }

    override fun createFileFromTemplate(name: String, template: FileTemplate, dir: PsiDirectory): PsiFile? {
        val project = dir.project

        // Remove .dsm extension if present and capitalize for namespace name
        val baseName = name.removeSuffix(".dsm")
        val capitalizedName = baseName.replaceFirstChar { it.uppercaseChar() }

        val properties = FileTemplateManager.getInstance(project).defaultProperties
        properties["NAME"] = capitalizedName
        properties["UUID"] = UUID.randomUUID().toString()

        val fileName = "$baseName.dsm"

        return try {
            val psiElement = FileTemplateUtil.createFromTemplate(template, fileName, properties, dir)
            psiElement.containingFile
        } catch (e: Exception) {
            null
        }
    }
}
