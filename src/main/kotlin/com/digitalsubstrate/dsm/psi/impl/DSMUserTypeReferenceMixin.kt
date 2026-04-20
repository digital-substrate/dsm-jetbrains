// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.psi.impl

import com.digitalsubstrate.dsm.psi.DSMElementFactory
import com.digitalsubstrate.dsm.psi.DSMReference
import com.digitalsubstrate.dsm.psi.DSMTypes
import com.digitalsubstrate.dsm.psi.DSMUserTypeReference
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiReference

/**
 * Mixin implementation for DSMUserTypeReference to provide reference resolution.
 * Implements PsiNameIdentifierOwner to support rename refactoring.
 */
abstract class DSMUserTypeReferenceMixin(node: ASTNode) : ASTWrapperPsiElement(node), DSMUserTypeReference, PsiNameIdentifierOwner {

    override fun getReference(): PsiReference {
        // TextRange should cover only the identifier part
        val id = identifier
        val startOffset = id.startOffsetInParent
        val endOffset = startOffset + id.textLength
        return DSMReference(this, TextRange(startOffset, endOffset))
    }

    override fun getNameIdentifier(): PsiElement? {
        return identifier
    }

    override fun getName(): String? {
        return identifier.text
    }

    override fun setName(name: String): PsiElement {
        val identifierNode = node.findChildByType(DSMTypes.IDENTIFIER)
        if (identifierNode != null) {
            // Create a new leaf node with the new name
            val newLeaf = com.intellij.psi.impl.source.tree.LeafPsiElement(DSMTypes.IDENTIFIER, name)
            node.replaceChild(identifierNode, newLeaf)
        }
        return this
    }

    override fun getTextOffset(): Int {
        return identifier.textOffset
    }
}
