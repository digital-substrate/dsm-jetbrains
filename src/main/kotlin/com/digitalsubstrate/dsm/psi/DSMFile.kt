package com.digitalsubstrate.dsm.psi

import com.digitalsubstrate.dsm.core.DSMFileType
import com.digitalsubstrate.dsm.core.DSMLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class DSMFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, DSMLanguage.INSTANCE) {
    override fun getFileType(): FileType = DSMFileType.INSTANCE

    override fun toString(): String = "DSM File"
}
