// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.psi

import com.digitalsubstrate.dsm.index.DSMNamedElementIndex
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.icons.AllIcons

/**
 * PSI Reference for DSM user-defined type references.
 * Handles navigation from type usages (e.g., key<Vertex>) to their declarations.
 * Supports both intra-file and inter-file navigation.
 */
class DSMReference(
    element: DSMUserTypeReference,
    textRange: TextRange
) : PsiReferenceBase<DSMUserTypeReference>(element, textRange), PsiPolyVariantReference {

    private val typeName: String
        get() = element.text.substring(rangeInElement.startOffset, rangeInElement.endOffset)

    /**
     * Resolve the reference to find the declaration of the type.
     * Searches for concept, struct, enum, club declarations across all project files.
     */
    override fun resolve(): PsiElement? {
        val results = multiResolve(false)
        return if (results.size == 1) results[0].element else null
    }

    /**
     * Multi-resolve to handle potential multiple declarations (though DSM should be unique).
     * Searches first in current file (fast), then across project (slower).
     */
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val file = element.containingFile as? DSMFile ?: return ResolveResult.EMPTY_ARRAY
        val declarations = mutableListOf<DSMNamedElement>()

        // STEP 1: Search in current file first (fast path)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                when (element) {
                    is DSMConceptDeclaration,
                    is DSMStructDeclaration,
                    is DSMEnumDeclaration,
                    is DSMClubDeclaration -> {
                        val namedElement = element as DSMNamedElement
                        if (namedElement.name == typeName) {
                            declarations.add(namedElement)
                        }
                    }
                }
                super.visitElement(element)
            }
        })

        // If found in current file, return immediately
        if (declarations.isNotEmpty()) {
            return declarations.map { PsiElementResolveResult(it) }.toTypedArray()
        }

        // STEP 2: Search across all project files using stub index (fast O(log n) lookup!)
        val project = element.project
        val foundElements = DSMNamedElementIndex.findElementsByName(project, typeName)
        declarations.addAll(foundElements)

        return declarations.map { PsiElementResolveResult(it) }.toTypedArray()
    }

    /**
     * Provide completion variants when typing a type reference.
     * Uses stub index for fast O(log n) lookup across all project files.
     */
    override fun getVariants(): Array<Any> {
        val project = element.project
        val variants = mutableListOf<LookupElement>()

        // Use stub index to get all names (ultra-fast HashMap lookup!)
        val allNames = DSMNamedElementIndex.getAllNames(project)

        // For each name, get the elements and create lookup variants
        allNames.forEach { name ->
            val elements = DSMNamedElementIndex.findElementsByName(project, name)
            elements.forEach { element ->
                when (element) {
                    is DSMConceptDeclaration -> {
                        variants.add(
                            LookupElementBuilder.create(name)
                                .withIcon(AllIcons.Nodes.Class)
                                .withTypeText("concept")
                        )
                    }
                    is DSMStructDeclaration -> {
                        variants.add(
                            LookupElementBuilder.create(name)
                                .withIcon(AllIcons.Nodes.Static)
                                .withTypeText("struct")
                        )
                    }
                    is DSMEnumDeclaration -> {
                        variants.add(
                            LookupElementBuilder.create(name)
                                .withIcon(AllIcons.Nodes.Enum)
                                .withTypeText("enum")
                        )
                    }
                    is DSMClubDeclaration -> {
                        variants.add(
                            LookupElementBuilder.create(name)
                                .withIcon(AllIcons.Nodes.Interface)
                                .withTypeText("club")
                        )
                    }
                }
            }
        }

        return variants.toTypedArray()
    }

    /**
     * Handle element rename by delegating to the ElementManipulator.
     * This is called when renaming a reference to update the reference text.
     */
    override fun handleElementRename(newElementName: String): PsiElement {
        return com.intellij.psi.ElementManipulators.handleContentChange(element, rangeInElement, newElementName) ?: element
    }
}
