// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.core

import com.intellij.lang.Language

class DSMLanguage private constructor() : Language("DSM") {
    companion object {
        @JvmStatic
        val INSTANCE = DSMLanguage()
    }

    override fun getDisplayName(): String = "DSM"

    override fun isCaseSensitive(): Boolean = true
}
