// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.stub

import com.digitalsubstrate.dsm.core.DSMLanguage
import com.digitalsubstrate.dsm.psi.DSMFile
import com.intellij.psi.stubs.PsiFileStubImpl
import com.intellij.psi.tree.IStubFileElementType

/**
 * Stub for a DSM file.
 * This is the root stub that contains all top-level declarations.
 */
class DSMFileStub(file: DSMFile?) : PsiFileStubImpl<DSMFile>(file) {
    override fun getType(): IStubFileElementType<DSMFileStub> = Type

    companion object {
        val Type = object : IStubFileElementType<DSMFileStub>(DSMLanguage.INSTANCE) {
            override fun getStubVersion(): Int = 1

            override fun getExternalId(): String = "dsm.FILE"
        }
    }
}
