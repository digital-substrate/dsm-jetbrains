// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.navigation

import com.digitalsubstrate.dsm.psi.*
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.digitalsubstrate.dsm.lexer.DSMLexerAdapter

/**
 * Provides Find Usages functionality for DSM language elements.
 */
class DSMFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            DSMLexerAdapter(),
            TokenSet.create(DSMTypes.IDENTIFIER),
            TokenSet.create(DSMTypes.LINE_COMMENT, DSMTypes.DOC_STRING),
            TokenSet.EMPTY
        )
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is DSMNamedElement
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return null
    }

    override fun getType(element: PsiElement): String {
        return when (element) {
            is DSMConceptDeclaration -> "concept"
            is DSMStructDeclaration -> "struct"
            is DSMEnumDeclaration -> "enum"
            is DSMClubDeclaration -> "club"
            is DSMFunctionPoolDeclaration -> "function_pool"
            is DSMAttachmentFunctionPoolDeclaration -> "attachment_function_pool"
            else -> ""
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return when (element) {
            is DSMNamedElement -> element.name ?: "<unnamed>"
            else -> ""
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return when (element) {
            is DSMNamedElement -> element.name ?: "<unnamed>"
            else -> ""
        }
    }
}
