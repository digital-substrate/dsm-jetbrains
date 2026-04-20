// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.util.IncorrectOperationException

/**
 * Element manipulator for DSMUserTypeReference.
 * Used by the rename refactoring to update type reference text.
 */
class DSMUserTypeReferenceManipulator : AbstractElementManipulator<DSMUserTypeReference>() {

    @Throws(IncorrectOperationException::class)
    override fun handleContentChange(
        element: DSMUserTypeReference,
        range: TextRange,
        newContent: String
    ): DSMUserTypeReference {
        val identifierNode = element.node.findChildByType(DSMTypes.IDENTIFIER) ?: return element

        // Create a new leaf node with the new text
        val newLeaf = LeafPsiElement(DSMTypes.IDENTIFIER, newContent)

        // Replace at AST level
        element.node.replaceChild(identifierNode, newLeaf)

        return element
    }

    override fun getRangeInElement(element: DSMUserTypeReference): TextRange {
        // Return the range of the identifier within the UserTypeReference element
        // This should match the range used in DSMUserTypeReferenceMixin.getReference()
        val identifier = element.identifier
        val startOffset = identifier.startOffsetInParent
        return TextRange(startOffset, startOffset + identifier.textLength)
    }
}
