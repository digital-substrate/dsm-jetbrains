// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.stub

import com.digitalsubstrate.dsm.psi.DSMNamedElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.NamedStubBase
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef

/**
 * Stub for DSM named elements (concept, struct, enum, club).
 * Contains only the name for efficient indexing.
 */
class DSMNamedElementStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
    name: StringRef?
) : NamedStubBase<DSMNamedElement>(parent, elementType, name) {

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String?
    ) : this(parent, elementType, StringRef.fromString(name))
}
