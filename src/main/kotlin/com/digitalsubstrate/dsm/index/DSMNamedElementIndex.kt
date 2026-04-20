// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.index

import com.digitalsubstrate.dsm.psi.*
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.psi.util.PsiTreeUtil

/**
 * Index for DSM named elements across all files in the project.
 * Enables cross-file navigation and reference resolution.
 */
class DSMNamedElementIndex : StringStubIndexExtension<DSMNamedElement>() {

    override fun getKey(): StubIndexKey<String, DSMNamedElement> = KEY

    companion object {
        val KEY = StubIndexKey.createIndexKey<String, DSMNamedElement>("dsm.named.element.index")

        /**
         * Find all DSM named elements with the given name in the project.
         */
        fun findElementsByName(
            project: Project,
            name: String,
            scope: GlobalSearchScope = GlobalSearchScope.allScope(project)
        ): Collection<DSMNamedElement> {
            return StubIndex.getElements(KEY, name, project, scope, DSMNamedElement::class.java)
        }

        /**
         * Get all DSM named element names in the project.
         */
        fun getAllNames(project: Project): Collection<String> {
            return StubIndex.getInstance().getAllKeys(KEY, project)
        }

        /**
         * Simple fallback search without stubs - scans all DSM files in project.
         * Used when stub index is not available.
         */
        fun findElementsByNameFallback(
            project: Project,
            name: String
        ): List<DSMNamedElement> {
            val result = mutableListOf<DSMNamedElement>()
            val psiManager = PsiManager.getInstance(project)

            // For now, we'll implement a simple file-based search
            // This will be slower but works without stubs
            com.intellij.psi.search.FileTypeIndex.getFiles(
                com.digitalsubstrate.dsm.core.DSMFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
            ).forEach { virtualFile ->
                    val psiFile = psiManager.findFile(virtualFile)
                    if (psiFile is DSMFile) {
                        // Search for named elements in this file
                        val namedElements = PsiTreeUtil.findChildrenOfType(psiFile, DSMNamedElement::class.java)
                        namedElements.filter { it.name == name }.forEach { result.add(it) }
                    }
                }

            return result
        }
    }
}
