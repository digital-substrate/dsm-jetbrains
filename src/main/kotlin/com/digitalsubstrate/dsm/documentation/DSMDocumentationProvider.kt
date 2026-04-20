// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.documentation

import com.digitalsubstrate.dsm.psi.*
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup.*
import com.intellij.psi.PsiElement

/**
 * Provides Quick Documentation (Ctrl+Q / F1) for DSM language elements.
 *
 * This provider extracts documentation from triple-quoted docstrings (""" ... """)
 * and presents it in a formatted popup with type information and context.
 */
class DSMDocumentationProvider : AbstractDocumentationProvider() {

    /**
     * Generates the main documentation content shown in the Quick Documentation popup.
     * Triggered by pressing F1 (macOS) or Ctrl+Q (Windows/Linux).
     */
    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return when (element) {
            is DSMConceptDeclaration -> generateConceptDoc(element)
            is DSMStructDeclaration -> generateStructDoc(element)
            is DSMEnumDeclaration -> generateEnumDoc(element)
            is DSMClubDeclaration -> generateClubDoc(element)
            is DSMAttachmentDeclaration -> generateAttachmentDoc(element)
            is DSMFunctionPoolDeclaration -> generateFunctionPoolDoc(element)
            is DSMAttachmentFunctionPoolDeclaration -> generateAttachmentFunctionPoolDoc(element)
            is DSMFieldDeclaration -> generateFieldDoc(element)
            is DSMFunctionDeclaration -> generateFunctionDoc(element)
            is DSMEnumValueDecl -> generateEnumValueDoc(element)
            else -> null
        }
    }

    /**
     * Generates quick navigation info shown on Ctrl/Cmd hover.
     * Returns plain text or simple HTML.
     */
    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement): String? {
        return when (element) {
            is DSMConceptDeclaration -> {
                val name = (element as? DSMNamedElement)?.name ?: "unknown"
                val file = element.containingFile.name
                val inheritance = element.conceptInheritance?.text ?: ""
                "concept $name $inheritance in $file"
            }
            is DSMStructDeclaration -> {
                val name = (element as? DSMNamedElement)?.name ?: "unknown"
                val file = element.containingFile.name
                "struct $name in $file"
            }
            is DSMEnumDeclaration -> {
                val name = (element as? DSMNamedElement)?.name ?: "unknown"
                val file = element.containingFile.name
                "enum $name in $file"
            }
            is DSMClubDeclaration -> {
                val name = (element as? DSMNamedElement)?.name ?: "unknown"
                val file = element.containingFile.name
                "club $name in $file"
            }
            is DSMFunctionPoolDeclaration -> {
                val name = (element as? DSMNamedElement)?.name ?: "unknown"
                "function_pool $name"
            }
            is DSMAttachmentFunctionPoolDeclaration -> {
                val name = (element as? DSMNamedElement)?.name ?: "unknown"
                "attachment_function_pool $name"
            }
            else -> null
        }
    }

    /**
     * Generates hover documentation (same as generateDoc for DSM).
     */
    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return generateDoc(element, originalElement)
    }

    // ==================== Private Documentation Generators ====================

    /**
     * Helper function to generate documentation with consistent structure.
     *
     * @param definitionHtml The HTML for the definition line (e.g., "<b>concept</b> Foo")
     * @param element The PSI element to extract docstring from
     * @param additionalSections Optional lambda to append additional sections
     */
    private fun buildDocumentation(
        definitionHtml: String,
        element: PsiElement,
        additionalSections: (StringBuilder.() -> Unit)? = null
    ): String = buildString {
        // Definition section
        append(DEFINITION_START)
        append(definitionHtml)
        append(DEFINITION_END)

        // Main content (docstring)
        val docstring = extractDocstring(element)
        if (docstring != null) {
            append(CONTENT_START)
            append(docstring)
            append(CONTENT_END)
        }

        // Additional sections
        append(SECTIONS_START)

        // File section (common to all)
        append(SECTION_HEADER_START)
        append("File")
        append(SECTION_SEPARATOR)
        append("<p>${element.containingFile.name}</p>")
        append(SECTION_END)

        // Custom sections
        additionalSections?.invoke(this)

        append(SECTIONS_END)
    }

    private fun generateConceptDoc(concept: DSMConceptDeclaration): String {
        val name = (concept as? DSMNamedElement)?.name ?: "unknown"
        val inheritance = concept.conceptInheritance?.text

        val definition = buildString {
            append("<b>concept</b> $name")
            if (inheritance != null) {
                append(" $inheritance")
            }
        }

        return buildDocumentation(definition, concept)
    }

    private fun generateStructDoc(struct: DSMStructDeclaration): String {
        val name = (struct as? DSMNamedElement)?.name ?: "unknown"
        return buildDocumentation("<b>struct</b> $name", struct)
    }

    private fun generateEnumDoc(enum: DSMEnumDeclaration): String {
        val name = (enum as? DSMNamedElement)?.name ?: "unknown"
        return buildDocumentation("<b>enum</b> $name", enum)
    }

    private fun generateEnumValueDoc(enumValue: DSMEnumValueDecl): String {
        val name = enumValue.identifierOrKeyword?.text ?: "unknown"
        // Enum values don't have a file section (they're inside enum declarations)
        return buildString {
            append(DEFINITION_START)
            append("<b>enum value</b> $name")
            append(DEFINITION_END)

            val docstring = extractDocstring(enumValue)
            if (docstring != null) {
                append(CONTENT_START)
                append(docstring)
                append(CONTENT_END)
            }
        }
    }

    private fun generateClubDoc(club: DSMClubDeclaration): String {
        val name = (club as? DSMNamedElement)?.name ?: "unknown"
        return buildDocumentation("<b>club</b> $name", club)
    }

    private fun generateAttachmentDoc(attachment: DSMAttachmentDeclaration): String {
        val name = (attachment as? DSMNamedElement)?.name ?: "unknown"
        return buildDocumentation("<b>attachment</b> $name", attachment)
    }

    private fun generateFunctionPoolDoc(pool: DSMFunctionPoolDeclaration): String {
        val name = (pool as? DSMNamedElement)?.name ?: "unknown"
        return buildDocumentation("<b>function_pool</b> $name", pool)
    }

    private fun generateAttachmentFunctionPoolDoc(pool: DSMAttachmentFunctionPoolDeclaration): String {
        val name = (pool as? DSMNamedElement)?.name ?: "unknown"
        return buildDocumentation("<b>attachment_function_pool</b> $name", pool)
    }

    private fun generateFieldDoc(field: DSMFieldDeclaration): String {
        val name = field.identifierOrKeyword?.text ?: "unknown"
        val type = field.typeReference?.text ?: "unknown"
        val defaultValue = field.defaultValue?.text

        val definition = buildString {
            append("$type <b>$name</b>")
            if (defaultValue != null) {
                append(" $defaultValue")
            }
        }

        // Fields are inside structs, so they don't have a "File" section
        // Instead, they have Type and Default sections
        return buildString {
            append(DEFINITION_START)
            append(definition)
            append(DEFINITION_END)

            val docstring = extractDocstring(field)
            if (docstring != null) {
                append(CONTENT_START)
                append(docstring)
                append(CONTENT_END)
            }

            append(SECTIONS_START)

            append(SECTION_HEADER_START)
            append("Type")
            append(SECTION_SEPARATOR)
            append("<p>$type</p>")
            append(SECTION_END)

            if (defaultValue != null) {
                append(SECTION_HEADER_START)
                append("Default")
                append(SECTION_SEPARATOR)
                append("<p>$defaultValue</p>")
                append(SECTION_END)
            }

            append(SECTIONS_END)
        }
    }

    private fun generateFunctionDoc(function: DSMFunctionDeclaration): String {
        val name = function.identifier?.text ?: "unknown"
        val params = function.parameterList?.text ?: "()"
        val returnType = function.typeReference?.text

        val definition = buildString {
            append("<b>function</b> $name$params")
            if (returnType != null) {
                append(": $returnType")
            }
        }

        // Functions are inside pools, so they don't have a "File" section
        return buildString {
            append(DEFINITION_START)
            append(definition)
            append(DEFINITION_END)

            val docstring = extractDocstring(function)
            if (docstring != null) {
                append(CONTENT_START)
                append(docstring)
                append(CONTENT_END)
            }
        }
    }

    // ==================== Docstring Extraction ====================

    /**
     * Extracts documentation using proper PSI tree navigation.
     * Delegates to DSMPsiUtil.getDocString() which navigates backwards through
     * previous siblings to find the DOC_STRING token.
     *
     * This follows IntelliJ Platform best practices (see development-guidelines.md):
     * "NEVER scan source text when a PSI tree exists"
     */
    private fun extractDocstring(element: PsiElement?): String? {
        if (element == null) return null
        return DSMPsiUtil.getDocString(element)
    }
}
