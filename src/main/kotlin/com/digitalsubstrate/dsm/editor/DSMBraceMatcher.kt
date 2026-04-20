// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.editor

import com.digitalsubstrate.dsm.psi.DSMTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

/**
 * Brace matcher for DSM language.
 *
 * Provides matching and highlighting for paired delimiters:
 * - { } for namespaces, structs, enums, function pools, list literals
 * - [ ] for arrays and type syntax
 * - ( ) for function parameters
 * - < > for generic type parameters
 *
 * Features:
 * - Highlights matching brace when cursor is on opening or closing brace
 * - Enables navigation between matching braces (Cmd+Shift+M / Ctrl+Shift+M)
 * - Supports "Select Enclosing Block" command
 */
class DSMBraceMatcher : PairedBraceMatcher {

    /**
     * Returns the pairs of braces that should be matched.
     */
    override fun getPairs(): Array<BracePair> {
        return arrayOf(
            BracePair(DSMTypes.LBRACE, DSMTypes.RBRACE, true),    // { } - structural
            BracePair(DSMTypes.LBRACKET, DSMTypes.RBRACKET, false), // [ ] - non-structural
            BracePair(DSMTypes.LPAREN, DSMTypes.RPAREN, false),   // ( ) - non-structural
            BracePair(DSMTypes.LANGLE, DSMTypes.RANGLE, false)    // < > - non-structural
        )
    }

    /**
     * Determines if matching should work before the given token type.
     *
     * @param lbraceType The left brace type (opening brace)
     * @param contextType The token type that follows the brace (can be null)
     * @return true if pairing should work at this position
     */
    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        // Allow brace pairing before most token types
        // Return true to enable auto-closing and matching for all braces
        return true
    }

    /**
     * Returns the starting offset for code construction when typing closing brace.
     *
     * This is used for smart indent and code completion.
     *
     * @param file The PSI file
     * @param openingBraceOffset The offset of the opening brace
     * @return The offset where code construction starts, or -1 if not applicable
     */
    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
        // Return the opening brace offset for structural braces
        // This enables smart indent when typing closing brace
        return openingBraceOffset
    }
}
