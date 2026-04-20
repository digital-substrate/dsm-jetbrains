// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.stub

import com.digitalsubstrate.dsm.core.DSMLanguage
import com.digitalsubstrate.dsm.index.DSMNamedElementIndex
import com.digitalsubstrate.dsm.psi.*
import com.digitalsubstrate.dsm.psi.impl.*
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.*
import com.intellij.util.io.StringRef
import java.io.IOException

/**
 * Base stub element type for DSM named elements.
 * Handles stub creation, serialization, and indexing.
 */
abstract class DSMNamedStubElementType<Psi : DSMNamedElement>(
    private val name: String
) : IStubElementType<DSMNamedElementStub, Psi>(name, DSMLanguage.INSTANCE) {

    override fun createStub(psi: Psi, parentStub: StubElement<*>?): DSMNamedElementStub {
        return DSMNamedElementStub(parentStub, this, psi.name)
    }

    @Throws(IOException::class)
    override fun serialize(stub: DSMNamedElementStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
    }

    @Throws(IOException::class)
    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): DSMNamedElementStub {
        val stubName = dataStream.readNameString()
        return DSMNamedElementStub(parentStub, this, StringRef.fromString(stubName))
    }

    override fun getExternalId(): String = "dsm.${name}"

    override fun indexStub(stub: DSMNamedElementStub, sink: IndexSink) {
        val name = stub.name
        if (name != null) {
            sink.occurrence(DSMNamedElementIndex.KEY, name)
        }
    }
}

/**
 * Stub element type for concept declarations.
 */
object DSMConceptStubElementType : DSMNamedStubElementType<DSMConceptDeclaration>("CONCEPT_DECLARATION") {
    override fun createPsi(stub: DSMNamedElementStub): DSMConceptDeclaration {
        return DSMConceptDeclarationImpl(stub, this)
    }
}

/**
 * Stub element type for struct declarations.
 */
object DSMStructStubElementType : DSMNamedStubElementType<DSMStructDeclaration>("STRUCT_DECLARATION") {
    override fun createPsi(stub: DSMNamedElementStub): DSMStructDeclaration {
        return DSMStructDeclarationImpl(stub, this)
    }
}

/**
 * Stub element type for enum declarations.
 */
object DSMEnumStubElementType : DSMNamedStubElementType<DSMEnumDeclaration>("ENUM_DECLARATION") {
    override fun createPsi(stub: DSMNamedElementStub): DSMEnumDeclaration {
        return DSMEnumDeclarationImpl(stub, this)
    }
}

/**
 * Stub element type for club declarations.
 */
object DSMClubStubElementType : DSMNamedStubElementType<DSMClubDeclaration>("CLUB_DECLARATION") {
    override fun createPsi(stub: DSMNamedElementStub): DSMClubDeclaration {
        return DSMClubDeclarationImpl(stub, this)
    }
}

/**
 * Factory function for Grammar Kit to create the appropriate stub element type.
 * This is called from DSM.bnf via elementTypeFactory attribute.
 */
fun factory(name: String): IStubElementType<*, *> {
    return when (name) {
        "CONCEPT_DECLARATION" -> DSMConceptStubElementType
        "STRUCT_DECLARATION" -> DSMStructStubElementType
        "ENUM_DECLARATION" -> DSMEnumStubElementType
        "CLUB_DECLARATION" -> DSMClubStubElementType
        else -> throw IllegalArgumentException("Unknown element type: $name")
    }
}
