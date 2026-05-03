package com.digitalsubstrate.dsm.psi

import com.digitalsubstrate.dsm.core.DSMLanguage
import com.intellij.psi.tree.IElementType

class DSMTokenType(debugName: String) : IElementType(debugName, DSMLanguage.INSTANCE) {
    override fun toString(): String = "DSMTokenType." + super.toString()
}
