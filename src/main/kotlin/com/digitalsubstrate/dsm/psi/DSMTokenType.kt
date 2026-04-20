// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.psi

import com.digitalsubstrate.dsm.core.DSMLanguage
import com.intellij.psi.tree.IElementType

class DSMTokenType(debugName: String) : IElementType(debugName, DSMLanguage.INSTANCE) {
    override fun toString(): String = "DSMTokenType." + super.toString()
}
