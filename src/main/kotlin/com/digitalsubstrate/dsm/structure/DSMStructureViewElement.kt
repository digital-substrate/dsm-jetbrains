package com.digitalsubstrate.dsm.structure

import com.digitalsubstrate.dsm.psi.*
import com.digitalsubstrate.dsm.services.DSMNamespaceService
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.icons.AllIcons

/**
 * Represents a node in the DSM structure view tree.
 */
class DSMStructureViewElement(private val psiElement: PsiElement) : StructureViewTreeElement, SortableTreeElement {

    override fun getValue(): Any = psiElement

    override fun navigate(requestFocus: Boolean) {
        if (psiElement is DSMFile || psiElement is DSMNamedElement || psiElement is DSMNamespaceDeclaration ||
            psiElement is DSMAttachmentDeclaration || psiElement is DSMFunctionDeclaration ||
            psiElement is DSMAttachmentFunctionDeclaration || psiElement is DSMFieldDeclaration ||
            psiElement is DSMEnumValueDecl) {
            (psiElement as? com.intellij.psi.NavigatablePsiElement)?.navigate(requestFocus)
        }
    }

    override fun canNavigate(): Boolean {
        return psiElement is com.intellij.psi.NavigatablePsiElement && psiElement.canNavigate()
    }

    override fun canNavigateToSource(): Boolean {
        return psiElement is com.intellij.psi.NavigatablePsiElement && psiElement.canNavigateToSource()
    }

    override fun getAlphaSortKey(): String = psiElement.text ?: ""

    override fun getPresentation(): ItemPresentation {
        return when (psiElement) {
            is DSMFile -> {
                PresentationData(psiElement.name, null, AllIcons.FileTypes.Any_type, null)
            }
            is DSMNamespaceDeclaration -> {
                val name = psiElement.identifier?.text ?: "<unnamed>"
                PresentationData(name, "namespace", AllIcons.Nodes.Package, null)
            }
            is DSMConceptDeclaration -> {
                val name = (psiElement as DSMNamedElement).name ?: "<unnamed>"
                PresentationData(name, "concept", AllIcons.Nodes.Class, null)
            }
            is DSMStructDeclaration -> {
                val name = (psiElement as DSMNamedElement).name ?: "<unnamed>"
                PresentationData(name, "struct", AllIcons.Nodes.Static, null)
            }
            is DSMEnumDeclaration -> {
                val name = (psiElement as DSMNamedElement).name ?: "<unnamed>"
                PresentationData(name, "enum", AllIcons.Nodes.Enum, null)
            }
            is DSMClubDeclaration -> {
                val name = (psiElement as DSMNamedElement).name ?: "<unnamed>"
                PresentationData(name, "club", AllIcons.Nodes.Interface, null)
            }
            is DSMFunctionPoolDeclaration -> {
                val name = (psiElement as DSMNamedElement).name ?: "<unnamed>"
                PresentationData(name, "function_pool", AllIcons.Nodes.Function, null)
            }
            is DSMAttachmentFunctionPoolDeclaration -> {
                val name = (psiElement as DSMNamedElement).name ?: "<unnamed>"
                PresentationData(name, "attachment_function_pool", AllIcons.Nodes.Function, null)
            }
            is DSMFunctionDeclaration -> {
                val name = psiElement.identifier?.text ?: "<unnamed>"
                val returnType = psiElement.typeReference.text
                val params = formatParameterList(psiElement.parameterList)
                val signature = "$returnType $name($params)"
                PresentationData(name, signature, AllIcons.Nodes.Method, null)
            }
            is DSMAttachmentFunctionDeclaration -> {
                val name = psiElement.identifier?.text ?: "<unnamed>"
                val returnType = psiElement.typeReference.text
                val params = formatParameterList(psiElement.parameterList)
                val signature = "$returnType $name($params)"
                PresentationData(name, signature, AllIcons.Nodes.Method, null)
            }
            is DSMFieldDeclaration -> {
                val name = psiElement.identifierOrKeyword.text
                val type = psiElement.typeReference.text
                val defaultVal = psiElement.defaultValue?.text ?: ""
                val signature = if (defaultVal.isNotEmpty()) {
                    "$type $defaultVal"
                } else {
                    type
                }
                PresentationData(name, signature, AllIcons.Nodes.Field, null)
            }
            is DSMEnumValueDecl -> {
                val name = psiElement.identifierOrKeyword.text
                PresentationData(name, "enum value", AllIcons.Nodes.Constant, null)
            }
            is DSMAttachmentDeclaration -> {
                val name = psiElement.identifier?.text ?: "<unnamed>"
                val types = psiElement.typeReferenceList
                val signature = if (types.size >= 2) {
                    "attachment<${types[0].text}, ${types[1].text}> $name"
                } else {
                    "attachment $name"
                }
                PresentationData(signature, null, AllIcons.Nodes.Annotationtype, null)
            }
            else -> {
                PresentationData(psiElement.text, null, null, null)
            }
        }
    }

    override fun getChildren(): Array<TreeElement> {
        if (psiElement is DSMFile) {
            // Get all top-level declarations
            val children = mutableListOf<TreeElement>()

            // Use DSMNamespaceService to group namespaces by UUID and name
            // Service handles all namespace merging logic (separation of concerns)
            val namespaceGroups = DSMNamespaceService.groupNamespacesByKey(psiElement)

            // Add one element per unique namespace (merged if multiple declarations)
            namespaceGroups.values.forEach { group ->
                if (group.isNotEmpty()) {
                    children.add(DSMStructureViewElement(group.first()))
                }
            }

            // Add top-level concepts
            PsiTreeUtil.getChildrenOfTypeAsList(psiElement, DSMConceptDeclaration::class.java)
                .forEach { children.add(DSMStructureViewElement(it)) }

            // Add top-level structs
            PsiTreeUtil.getChildrenOfTypeAsList(psiElement, DSMStructDeclaration::class.java)
                .forEach { children.add(DSMStructureViewElement(it)) }

            // Add top-level enums
            PsiTreeUtil.getChildrenOfTypeAsList(psiElement, DSMEnumDeclaration::class.java)
                .forEach { children.add(DSMStructureViewElement(it)) }

            // Add top-level clubs
            PsiTreeUtil.getChildrenOfTypeAsList(psiElement, DSMClubDeclaration::class.java)
                .forEach { children.add(DSMStructureViewElement(it)) }

            // Add top-level function pools
            PsiTreeUtil.getChildrenOfTypeAsList(psiElement, DSMFunctionPoolDeclaration::class.java)
                .forEach { children.add(DSMStructureViewElement(it)) }

            // Add top-level attachment function pools
            PsiTreeUtil.getChildrenOfTypeAsList(psiElement, DSMAttachmentFunctionPoolDeclaration::class.java)
                .forEach { children.add(DSMStructureViewElement(it)) }

            // Add top-level attachments
            PsiTreeUtil.getChildrenOfTypeAsList(psiElement, DSMAttachmentDeclaration::class.java)
                .forEach { children.add(DSMStructureViewElement(it)) }

            return children.toTypedArray()
        }

        if (psiElement is DSMNamespaceDeclaration) {
            // Find the containing file
            val containingFile = psiElement.containingFile
            if (containingFile !is DSMFile) return emptyArray()

            // Use DSMNamespaceService to find all namespace declarations with same key
            // and collect all their children (separation of concerns)
            val sameNamespaces = DSMNamespaceService.findNamespacesWithSameKey(psiElement, containingFile)
            val childElements = DSMNamespaceService.collectAllChildren(sameNamespaces)

            // Convert PSI elements to TreeElements
            return childElements.map { DSMStructureViewElement(it) }.toTypedArray()
        }

        // Structs: show their fields as children
        if (psiElement is DSMStructDeclaration) {
            val children = mutableListOf<TreeElement>()
            PsiTreeUtil.getChildrenOfTypeAsList(psiElement, DSMFieldDeclaration::class.java)
                .forEach { children.add(DSMStructureViewElement(it)) }
            return children.toTypedArray()
        }

        // Enums: show their values as children
        if (psiElement is DSMEnumDeclaration) {
            val children = mutableListOf<TreeElement>()
            val enumValueList = psiElement.enumValueList
            if (enumValueList != null) {
                PsiTreeUtil.getChildrenOfTypeAsList(enumValueList, DSMEnumValueDecl::class.java)
                    .forEach { children.add(DSMStructureViewElement(it)) }
            }
            return children.toTypedArray()
        }

        // Function pools: show their functions as children
        if (psiElement is DSMFunctionPoolDeclaration) {
            val children = mutableListOf<TreeElement>()
            PsiTreeUtil.getChildrenOfTypeAsList(psiElement, DSMFunctionDeclaration::class.java)
                .forEach { children.add(DSMStructureViewElement(it)) }
            return children.toTypedArray()
        }

        // Attachment function pools: show their functions as children
        if (psiElement is DSMAttachmentFunctionPoolDeclaration) {
            val children = mutableListOf<TreeElement>()
            PsiTreeUtil.getChildrenOfTypeAsList(psiElement, DSMAttachmentFunctionDeclaration::class.java)
                .forEach { children.add(DSMStructureViewElement(it)) }
            return children.toTypedArray()
        }

        // Concepts and clubs don't have children in the structure view
        return emptyArray()
    }

    /**
     * Format parameter list for function signatures.
     * Extracts type and name for each parameter and formats as: "type name, type name, ..."
     */
    private fun formatParameterList(parameterList: DSMParameterList?): String {
        if (parameterList == null) return ""

        val parameters = PsiTreeUtil.getChildrenOfTypeAsList(parameterList, DSMParameter::class.java)
        if (parameters.isEmpty()) return ""

        return parameters.joinToString(", ") { param ->
            val type = param.typeReference.text
            val name = param.identifierOrKeyword?.text ?: "param"
            "$type $name"
        }
    }
}
