// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.completion

import com.digitalsubstrate.dsm.psi.*
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiManager
import com.digitalsubstrate.dsm.core.DSMFileType
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext

class DSMCompletionContributor : CompletionContributor() {

    init {
        // Keywords completion
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(),
            KeywordCompletionProvider()
        )

        // Type completion (context-aware)
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(),
            ContextAwareTypeCompletionProvider()
        )

        // Concept/Club completion for key<T> and attachment<T, ...>
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(),
            ConceptCompletionProvider()
        )
    }

    companion object {
        /**
         * Check if we're inside a type parameter that requires concept/club types:
         * - key<T>
         * - attachment<T, D> (first parameter T only)
         */
        fun isInsideConceptTypeContext(position: com.intellij.psi.PsiElement): Boolean {
            // Check 1: key<T> context
            if (isInsideKeyGenericType(position)) return true

            // Check 2: attachment<T, D> first parameter context
            if (isInsideAttachmentFirstParameter(position)) return true

            return false
        }

        /**
         * Check if we're inside a key<T> generic type.
         */
        private fun isInsideKeyGenericType(position: com.intellij.psi.PsiElement): Boolean {
            // PSI-based: walk up to find DSMGenericType starting with "key<"
            var current: com.intellij.psi.PsiElement? = position
            while (current != null) {
                if (current is DSMGenericType) {
                    val text = current.text
                    if (text.startsWith("key<") || text.startsWith("key ")) {
                        return true
                    }
                }
                current = current.parent
            }

            // Text-based fallback for incomplete PSI
            val file = position.containingFile ?: return false
            val offset = position.textOffset
            if (offset < 4) return false

            val textBefore = file.text.substring(maxOf(0, offset - 50), offset)

            // Check for key< context (not closed yet)
            if (textBefore.contains("key<")) {
                val afterKey = textBefore.substringAfterLast("key<")
                if (!afterKey.contains(">")) {
                    return true
                }
            }

            return false
        }

        /**
         * Check if we're inside the FIRST type parameter of attachment<T, D>.
         * T must be a concept, club, or any_concept.
         * D can be any type.
         */
        private fun isInsideAttachmentFirstParameter(position: com.intellij.psi.PsiElement): Boolean {
            // PSI-based: walk up to find DSMAttachmentDeclaration and check parameter position
            var current: com.intellij.psi.PsiElement? = position
            var containingTypeRef: DSMTypeReference? = null

            while (current != null) {
                // Track the first DSMTypeReference we encounter going up
                if (current is DSMTypeReference && containingTypeRef == null) {
                    containingTypeRef = current
                }

                if (current is DSMAttachmentDeclaration) {
                    val typeRefs = current.typeReferenceList
                    // First type reference (index 0) = T (concept/club)
                    // Second type reference (index 1) = D (any type)
                    if (typeRefs.isNotEmpty() && containingTypeRef != null) {
                        return containingTypeRef == typeRefs.firstOrNull()
                    }
                    return false
                }

                current = current.parent
            }

            // Text-based fallback for incomplete PSI during typing
            return isInAttachmentFirstParameterByText(position)
        }

        /**
         * Text-based detection for attachment<T, D> first parameter.
         * Used when PSI tree is incomplete during typing.
         */
        private fun isInAttachmentFirstParameterByText(position: com.intellij.psi.PsiElement): Boolean {
            val file = position.containingFile ?: return false
            val offset = position.textOffset
            if (offset < 11) return false // "attachment<" is 11 chars

            val textBefore = file.text.substring(maxOf(0, offset - 100), offset)

            val attachmentIdx = textBefore.lastIndexOf("attachment<")
            if (attachmentIdx < 0) return false

            val afterAttachment = textBefore.substring(attachmentIdx + "attachment<".length)

            // Count angle bracket depth to handle nested generics like:
            // attachment<Concept, map<int64, string>>
            // We want to detect commas at depth 0 (the attachment's own comma)
            var depth = 0
            var commaAtRootLevel = false

            for (char in afterAttachment) {
                when (char) {
                    '<' -> depth++
                    '>' -> {
                        if (depth == 0) {
                            // Closed the attachment, we're outside
                            return false
                        }
                        depth--
                    }
                    ',' -> if (depth == 0) commaAtRootLevel = true
                }
            }

            // We're inside attachment<...> and there's no comma at root level yet
            // This means we're still in the first parameter (T)
            return !commaAtRootLevel
        }
    }

    private class KeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            resultSet: CompletionResultSet
        ) {
            // Skip keywords in concept-only context
            if (isInsideConceptTypeContext(parameters.position)) {
                return
            }

            // Only keywords NOT covered by Live Templates
            val keywords = listOf(
                "is a",  // Keep for inheritance syntax
                "true", "false"  // Boolean literals
            )

            keywords.forEach { keyword ->
                resultSet.addElement(
                    LookupElementBuilder.create(keyword)
                        .bold()
                        .withTypeText("keyword")
                )
            }
        }
    }

    private class ContextAwareTypeCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            resultSet: CompletionResultSet
        ) {
            // Skip primitive types if we're in a concept-only context (key<T>, attachment<T, ...>)
            if (isInsideConceptTypeContext(parameters.position)) {
                return
            }

            // Primitive types only - generic types (vec, mat, key, optional, etc.)
            // are handled by Live Templates which provide better UX with dropdowns
            val primitiveTypes = listOf(
                "void", "bool",
                "int8", "int16", "int32", "int64",
                "uint8", "uint16", "uint32", "uint64",
                "float", "double",
                "string", "uuid", "blob_id", "commit_id",
                "any", "any_concept"
            )

            primitiveTypes.forEach { type ->
                resultSet.addElement(
                    LookupElementBuilder.create(type)
                        .withTypeText("primitive type")
                        .withIcon(AllIcons.Nodes.Type)
                )
            }
        }
    }

    /**
     * Provides concept/club completion inside key<T> and attachment<T, ...> type parameters.
     */
    private class ConceptCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            resultSet: CompletionResultSet
        ) {
            val position = parameters.position
            val project = position.project

            // Check if we're inside a context that requires concept/club types
            if (!isInsideConceptTypeContext(position)) {
                return
            }

            // Add any_concept as a valid option
            resultSet.addElement(
                LookupElementBuilder.create("any_concept")
                    .withTypeText("any concept")
                    .withIcon(AllIcons.Nodes.Type)
                    .bold()
            )

            // Find all concepts and clubs in the project
            val psiManager = PsiManager.getInstance(project)
            val scope = GlobalSearchScope.allScope(project)

            FileTypeIndex.getFiles(DSMFileType.INSTANCE, scope).forEach { virtualFile ->
                val psiFile = psiManager.findFile(virtualFile)
                if (psiFile is DSMFile) {
                    // Find concepts
                    PsiTreeUtil.findChildrenOfType(psiFile, DSMConceptDeclaration::class.java)
                        .forEach { concept ->
                            concept.name?.let { name ->
                                resultSet.addElement(
                                    LookupElementBuilder.create(name)
                                        .withTypeText("concept")
                                        .withIcon(AllIcons.Nodes.Class)
                                        .withTailText(" (${virtualFile.name})", true)
                                )
                            }
                        }

                    // Find clubs
                    PsiTreeUtil.findChildrenOfType(psiFile, DSMClubDeclaration::class.java)
                        .forEach { club ->
                            club.name?.let { name ->
                                resultSet.addElement(
                                    LookupElementBuilder.create(name)
                                        .withTypeText("club")
                                        .withIcon(AllIcons.Nodes.Interface)
                                        .withTailText(" (${virtualFile.name})", true)
                                )
                            }
                        }
                }
            }

            // Stop other completion contributors (e.g., IntelliJ's default word completion)
            // to prevent structs, enums, and other non-concept types from appearing
            resultSet.stopHere()
        }
    }
}
