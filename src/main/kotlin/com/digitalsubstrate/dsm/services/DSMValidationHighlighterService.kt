package com.digitalsubstrate.dsm.services

import com.digitalsubstrate.dsm.services.DSMValidationService
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.MarkupModel
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.ui.JBColor
import java.awt.Font
import java.io.File

/**
 * Service to manage error highlighters in the editor for DSM validation errors.
 */
@Service(Service.Level.PROJECT)
class DSMValidationHighlighterService(private val project: Project) {

    private val highlighters = mutableListOf<RangeHighlighter>()

    /**
     * Clear all existing highlighters from previous validation.
     */
    fun clearHighlighters() {
        ApplicationManager.getApplication().invokeLater {
            highlighters.forEach { it.dispose() }
            highlighters.clear()
        }
    }

    /**
     * Add highlighters for validation errors in the editor.
     */
    fun addHighlighters(errors: List<DSMValidationService.ValidationError>) {
        ApplicationManager.getApplication().invokeLater {
            for (error in errors) {
                addErrorHighlighter(error)
            }
        }
    }

    private fun addErrorHighlighter(error: DSMValidationService.ValidationError) {
        val file = File(error.filePath)
        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file) ?: return
        val document = FileDocumentManager.getInstance().getDocument(virtualFile) ?: return

        // Calculate offset from line and column (0-based)
        val line = error.line - 1  // Convert to 0-based
        val column = error.column - 1  // Convert to 0-based

        if (line < 0 || line >= document.lineCount) return

        val lineStartOffset = document.getLineStartOffset(line)
        val lineEndOffset = document.getLineEndOffset(line)
        val offset = (lineStartOffset + column).coerceIn(lineStartOffset, lineEndOffset)

        // Find the end of the word at this position
        val text = document.charsSequence
        var endOffset = offset
        while (endOffset < lineEndOffset && text[endOffset].isLetterOrDigit()) {
            endOffset++
        }
        if (endOffset == offset) {
            endOffset = (offset + 1).coerceAtMost(lineEndOffset)
        }

        // Get all editors for this document
        val editorFactory = com.intellij.openapi.editor.EditorFactory.getInstance()
        val allEditors = editorFactory.allEditors

        for (editor in allEditors) {
            if (editor.document != document) continue
            if (editor.project != project) continue

            // Create error attributes (red wavy underline)
            val attributes = TextAttributes().apply {
                effectColor = JBColor.RED
                effectType = com.intellij.openapi.editor.markup.EffectType.WAVE_UNDERSCORE
            }

            // Add highlighter to this editor's markup model
            val highlighter = editor.markupModel.addRangeHighlighter(
                offset,
                endOffset,
                HighlighterLayer.ERROR,
                attributes,
                HighlighterTargetArea.EXACT_RANGE
            )

            highlighter.errorStripeTooltip = error.message

            highlighters.add(highlighter)
        }
    }

    companion object {
        fun getInstance(project: Project): DSMValidationHighlighterService {
            return project.getService(DSMValidationHighlighterService::class.java)
        }
    }
}
