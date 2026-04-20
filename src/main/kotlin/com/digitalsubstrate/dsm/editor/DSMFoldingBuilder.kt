// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.editor

import com.digitalsubstrate.dsm.psi.*
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Provides code folding for DSM files.
 * Allows collapsing/expanding namespaces, structs, enums, clubs, and function pools.
 */
class DSMFoldingBuilder : FoldingBuilderEx() {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()

        // Find all foldable elements in the file
        PsiTreeUtil.findChildrenOfAnyType(
            root,
            DSMNamespaceDeclaration::class.java,
            DSMStructDeclaration::class.java,
            DSMEnumDeclaration::class.java,
            DSMClubDeclaration::class.java,
            DSMConceptDeclaration::class.java,
            DSMFunctionPoolDeclaration::class.java,
            DSMAttachmentFunctionPoolDeclaration::class.java,
            DSMFunctionDeclaration::class.java,
            DSMAttachmentFunctionDeclaration::class.java
        ).forEach { element ->
            val foldingRange = getFoldingRange(element)
            if (foldingRange != null && !foldingRange.isEmpty) {
                descriptors.add(FoldingDescriptor(element.node, foldingRange))
            }
        }

        return descriptors.toTypedArray()
    }

    /**
     * Get the text range to fold for a given element.
     * Returns the range from opening brace to closing brace, or null if not foldable.
     *
     * Uses PSI tree navigation instead of text scanning, following IntelliJ Platform best practices.
     */
    private fun getFoldingRange(element: PsiElement): TextRange? {
        // Find opening and closing braces using PSI tree navigation (NOT text scanning)
        val lbrace = element.node.findChildByType(DSMTypes.LBRACE) ?: return null
        val rbrace = element.node.findChildByType(DSMTypes.RBRACE) ?: return null

        // Return range from opening brace to closing brace
        return TextRange(lbrace.startOffset, rbrace.startOffset + 1)
    }

    /**
     * Placeholder text shown when the region is folded.
     */
    override fun getPlaceholderText(node: ASTNode): String {
        val element = node.psi

        return when (element) {
            is DSMNamespaceDeclaration -> buildPlaceholder("namespace", element.identifier?.text)
            is DSMStructDeclaration -> buildPlaceholder("struct", (element as? DSMNamedElement)?.name)
            is DSMEnumDeclaration -> buildPlaceholder("enum", (element as? DSMNamedElement)?.name)
            is DSMClubDeclaration -> buildPlaceholder("club", (element as? DSMNamedElement)?.name)
            is DSMConceptDeclaration -> buildPlaceholder("concept", (element as? DSMNamedElement)?.name)
            is DSMFunctionPoolDeclaration -> buildPlaceholder("function_pool", (element as? DSMNamedElement)?.name)
            is DSMAttachmentFunctionPoolDeclaration -> buildPlaceholder("attachment_function_pool", (element as? DSMNamedElement)?.name)
            is DSMFunctionDeclaration -> buildPlaceholder(null, element.identifier?.text)
            is DSMAttachmentFunctionDeclaration -> buildPlaceholder(null, element.identifier?.text)
            else -> "{ ... }"
        }
    }

    /**
     * Helper function to build consistent placeholder text for folded regions.
     * @param type The type keyword (e.g., "struct", "enum"), or null for functions
     * @param name The element name, or null if unnamed
     */
    private fun buildPlaceholder(type: String?, name: String?): String {
        val elementName = name ?: "unnamed"
        return if (type != null) {
            "{ $type $elementName ... }"
        } else {
            "{ $elementName ... }"
        }
    }

    /**
     * Whether the folding region is collapsed by default.
     * Return false to keep everything expanded initially.
     */
    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}
