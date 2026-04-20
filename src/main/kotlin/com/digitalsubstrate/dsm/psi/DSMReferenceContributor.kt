// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.psi

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

/**
 * Contributes PSI references for DSM user-defined types.
 * This ensures that Find Usages and other reference-based features work correctly.
 */
class DSMReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(DSMUserTypeReference::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val userTypeRef = element as? DSMUserTypeReference ?: return PsiReference.EMPTY_ARRAY
                    val reference = userTypeRef.reference ?: return PsiReference.EMPTY_ARRAY
                    return arrayOf(reference)
                }
            }
        )
    }
}
