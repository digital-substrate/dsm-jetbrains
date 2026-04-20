// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.core

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class DSMFileType private constructor() : LanguageFileType(DSMLanguage.INSTANCE) {
    override fun getName(): String = "DSM File"

    override fun getDescription(): String = "Digital Substrate Model file"

    override fun getDefaultExtension(): String = "dsm"

    override fun getIcon(): Icon = DSMIcons.FILE

    companion object {
        @JvmStatic
        val INSTANCE = DSMFileType()
    }
}
