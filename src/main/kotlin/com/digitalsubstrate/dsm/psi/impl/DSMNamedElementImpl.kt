package com.digitalsubstrate.dsm.psi.impl

import com.digitalsubstrate.dsm.psi.*
import com.digitalsubstrate.dsm.stub.DSMNamedElementStub
import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.stubs.IStubElementType
import javax.swing.Icon

/**
 * Mixin implementation for DSMNamedElement.
 * Supports both AST-based and stub-based PSI elements.
 * Provides default implementations for name-related operations.
 */
abstract class DSMNamedElementImpl : StubBasedPsiElementBase<DSMNamedElementStub>, DSMNamedElement {

    constructor(node: ASTNode) : super(node)

    constructor(stub: DSMNamedElementStub, stubType: IStubElementType<*, *>) : super(stub, stubType)

    override fun getName(): String? {
        // Use stub if available (fast!), otherwise parse AST
        val stub = greenStub
        if (stub != null) {
            return stub.name
        }
        return nameIdentifier?.text
    }

    override fun setName(name: String): PsiElement {
        val identifierNode = node.findChildByType(DSMTypes.IDENTIFIER)
        if (identifierNode != null) {
            // Create a new leaf node with the new name
            val newLeaf = LeafPsiElement(DSMTypes.IDENTIFIER, name)
            // Replace the old identifier node with the new one at the AST level
            node.replaceChild(identifierNode, newLeaf)
        }
        return this
    }

    override fun getNameIdentifier(): PsiElement? {
        // Find the IDENTIFIER token in this element's children
        return node.findChildByType(DSMTypes.IDENTIFIER)?.psi
    }

    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()

    /**
     * Returns the icon for this element based on its type.
     * Used by Symbol Search, Structure View, and other navigation features.
     */
    override fun getIcon(flags: Int): Icon? {
        return when (this) {
            is DSMConceptDeclaration -> AllIcons.Nodes.Class
            is DSMStructDeclaration -> AllIcons.Nodes.Static
            is DSMEnumDeclaration -> AllIcons.Nodes.Enum
            is DSMClubDeclaration -> AllIcons.Nodes.Interface
            is DSMFunctionPoolDeclaration -> AllIcons.Nodes.Function
            is DSMAttachmentFunctionPoolDeclaration -> AllIcons.Nodes.Function
            else -> null
        }
    }

    /**
     * Provides presentation for Symbol Search (Navigate | Symbol).
     * Returns name, file location, and appropriate icon based on element type.
     */
    override fun getPresentation(): ItemPresentation? {
        val name = getName() ?: return null
        val location = containingFile?.name
        val icon = getIcon(0)  // Delegate to getIcon()

        return PresentationData(name, location, icon, null)
    }
}
