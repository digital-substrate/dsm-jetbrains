package com.digitalsubstrate.dsm.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.digitalsubstrate.dsm.core.DSMFileType

/**
 * Factory for creating DSM PSI elements programmatically.
 * Used primarily for rename refactoring and code manipulation.
 */
object DSMElementFactory {

    /**
     * Creates an IDENTIFIER element with the given name.
     * This is used for rename refactoring to replace old identifiers with new ones.
     */
    fun createIdentifier(project: Project, name: String): PsiElement? {
        // Create a dummy concept declaration with the desired identifier
        val dummyFile = createFile(project, "concept $name;")

        // Find the concept declaration in the dummy file
        var conceptDecl: DSMConceptDeclaration? = null
        dummyFile.accept(object : com.intellij.psi.PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element is DSMConceptDeclaration) {
                    conceptDecl = element
                }
                super.visitElement(element)
            }
        })

        // Extract and return the identifier element
        return conceptDecl?.identifier
    }

    /**
     * Creates a temporary DSM file with the given text.
     * Used internally to extract specific PSI elements.
     */
    private fun createFile(project: Project, text: String): DSMFile {
        val name = "dummy.dsm"
        return PsiFileFactory.getInstance(project)
            .createFileFromText(name, DSMFileType.INSTANCE, text) as DSMFile
    }
}
