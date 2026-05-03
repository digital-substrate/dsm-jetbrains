package com.digitalsubstrate.dsm.services

import com.digitalsubstrate.dsm.psi.*
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Service for namespace-related operations.
 * Extracts namespace merging logic from view layer (separation of concerns).
 *
 * Namespaces in DSM can be declared multiple times in a file with the same UUID and name.
 * This service handles the logic of grouping and merging these declarations.
 */
object DSMNamespaceService {

    /**
     * Get the unique key for a namespace declaration.
     * Key is composed of both UUID and name to ensure strict matching.
     *
     * @return Composite key in format "UUID::NAME"
     */
    fun getNamespaceKey(namespace: DSMNamespaceDeclaration): String {
        val uuid = namespace.uuid?.text ?: "NO_UUID"
        val name = namespace.identifier?.text ?: "NO_NAME"
        return "$uuid::$name"
    }

    /**
     * Group all namespace declarations in a file by their composite key.
     * Multiple declarations with the same UUID and name will be grouped together.
     *
     * @param file The DSM file containing namespace declarations
     * @return Map of namespace key to list of declarations
     */
    fun groupNamespacesByKey(file: DSMFile): Map<String, List<DSMNamespaceDeclaration>> {
        val allNamespaces = PsiTreeUtil.getChildrenOfTypeAsList(file, DSMNamespaceDeclaration::class.java)
        return allNamespaces.groupBy { getNamespaceKey(it) }
    }

    /**
     * Find all namespace declarations with the same key as the given namespace.
     *
     * @param namespace The namespace declaration to match
     * @param file The file to search in
     * @return List of namespace declarations with matching key
     */
    fun findNamespacesWithSameKey(
        namespace: DSMNamespaceDeclaration,
        file: DSMFile
    ): List<DSMNamespaceDeclaration> {
        val targetKey = getNamespaceKey(namespace)
        val allNamespaces = PsiTreeUtil.getChildrenOfTypeAsList(file, DSMNamespaceDeclaration::class.java)
        return allNamespaces.filter { getNamespaceKey(it) == targetKey }
    }

    /**
     * Collect all children declarations from a list of namespace declarations.
     * Merges children from multiple namespace blocks with the same key.
     *
     * @param namespaces List of namespace declarations to collect from
     * @return List of all child declarations (concepts, structs, enums, etc.)
     */
    fun collectAllChildren(namespaces: List<DSMNamespaceDeclaration>): List<PsiElement> {
        val children = mutableListOf<PsiElement>()

        for (namespace in namespaces) {
            val namespaceBody = namespace.namespaceBody ?: continue

            // Add concepts
            children.addAll(
                PsiTreeUtil.getChildrenOfTypeAsList(namespaceBody, DSMConceptDeclaration::class.java)
            )

            // Add structs
            children.addAll(
                PsiTreeUtil.getChildrenOfTypeAsList(namespaceBody, DSMStructDeclaration::class.java)
            )

            // Add enums
            children.addAll(
                PsiTreeUtil.getChildrenOfTypeAsList(namespaceBody, DSMEnumDeclaration::class.java)
            )

            // Add clubs
            children.addAll(
                PsiTreeUtil.getChildrenOfTypeAsList(namespaceBody, DSMClubDeclaration::class.java)
            )

            // Add function pools
            children.addAll(
                PsiTreeUtil.getChildrenOfTypeAsList(namespaceBody, DSMFunctionPoolDeclaration::class.java)
            )

            // Add attachment function pools
            children.addAll(
                PsiTreeUtil.getChildrenOfTypeAsList(namespaceBody, DSMAttachmentFunctionPoolDeclaration::class.java)
            )

            // Add attachments
            children.addAll(
                PsiTreeUtil.getChildrenOfTypeAsList(namespaceBody, DSMAttachmentDeclaration::class.java)
            )
        }

        return children
    }
}
