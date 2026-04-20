// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import javax.swing.Icon

/**
 * Interface for DSM elements that have a name and can be referenced.
 * This includes concepts, structs, enums, clubs, function pools, etc.
 * Implements NavigationItem to support Symbol Search (Navigate | Symbol).
 */
interface DSMNamedElement : PsiNameIdentifierOwner, NavigationItem {
    override fun getName(): String?
    override fun setName(name: String): PsiElement
    override fun getNameIdentifier(): PsiElement?
    override fun getTextOffset(): Int
    override fun getPresentation(): ItemPresentation?

    /**
     * Returns the icon for this element.
     * Used by Symbol Search and other navigation features.
     */
    override fun getIcon(flags: Int): Icon?
}
