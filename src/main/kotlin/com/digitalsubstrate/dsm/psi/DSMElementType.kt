package com.digitalsubstrate.dsm.psi

import com.digitalsubstrate.dsm.core.DSMLanguage
import com.intellij.psi.tree.IElementType

class DSMElementType(debugName: String) : IElementType(debugName, DSMLanguage.INSTANCE) {
    override fun toString(): String = "DSMElementType." + super.toString()
}
