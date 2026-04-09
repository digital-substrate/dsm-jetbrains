package com.digitalsubstrate.dsm.navigation

import com.digitalsubstrate.dsm.psi.DSMFile
import com.digitalsubstrate.dsm.psi.DSMNamedElement
import com.intellij.navigation.ChooseByNameContributorEx
import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.Processor
import com.intellij.util.indexing.FindSymbolParameters
import com.intellij.util.indexing.IdFilter

/**
 * Contributor for "Navigate | Symbol" (Cmd+O / Ctrl+N) action.
 * Enables quick navigation to DSM types (concepts, structs, enums, clubs, function pools).
 *
 * Implementation based on IntelliJ Platform SDK documentation:
 * https://plugins.jetbrains.com/docs/intellij/go-to-symbol-contributor.html
 *
 * NOTE: This implementation scans files directly without using stub indexes,
 * as DSM plugin does not have stubs configured yet.
 */
class DSMChooseByNameContributor : ChooseByNameContributorEx {

    /**
     * Collects all symbol names from DSM files in the project.
     * Called by the IDE to populate the search dialog.
     */
    override fun processNames(
        processor: Processor<in String>,
        scope: GlobalSearchScope,
        filter: IdFilter?
    ) {
        val project = scope.project ?: return
        val psiManager = PsiManager.getInstance(project)
        val namesProcessed = mutableSetOf<String>()

        // Scan all .dsm files in the project
        FilenameIndex.getAllFilesByExt(project, "dsm", scope).forEach { virtualFile ->
            val psiFile = psiManager.findFile(virtualFile)
            if (psiFile is DSMFile) {
                // Find all named elements in this file
                val namedElements = PsiTreeUtil.findChildrenOfType(psiFile, DSMNamedElement::class.java)
                namedElements.forEach { element ->
                    val name = element.name
                    if (name != null && !namesProcessed.contains(name)) {
                        namesProcessed.add(name)
                        if (!processor.process(name)) {
                            return  // Stop if processor returns false
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns navigation items (PSI elements) matching the given name.
     * Called when user selects an item from the search results.
     */
    override fun processElementsWithName(
        name: String,
        processor: Processor<in NavigationItem>,
        parameters: FindSymbolParameters
    ) {
        val project = parameters.project
        val scope = parameters.searchScope
        val psiManager = PsiManager.getInstance(project)

        // Scan all .dsm files to find elements with this name
        FilenameIndex.getAllFilesByExt(project, "dsm", scope).forEach { virtualFile ->
            val psiFile = psiManager.findFile(virtualFile)
            if (psiFile is DSMFile) {
                // Find all named elements with matching name in this file
                val namedElements = PsiTreeUtil.findChildrenOfType(psiFile, DSMNamedElement::class.java)
                namedElements.filter { it.name == name }.forEach { element ->
                    if (!processor.process(element)) {
                        return  // Stop if processor returns false
                    }
                }
            }
        }
    }
}
