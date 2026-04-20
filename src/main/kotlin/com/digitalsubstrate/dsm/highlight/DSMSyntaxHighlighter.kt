// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.highlight

import com.digitalsubstrate.dsm.lexer.DSMLexerAdapter
import com.digitalsubstrate.dsm.psi.DSMTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class DSMSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        // Keywords
        val KEYWORD = createTextAttributesKey("DSM_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)

        // Type keywords (primitive and generic)
        val TYPE_KEYWORD = createTextAttributesKey("DSM_TYPE_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)

        // Identifiers
        val IDENTIFIER = createTextAttributesKey("DSM_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)

        // Literals
        val NUMBER = createTextAttributesKey("DSM_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val STRING = createTextAttributesKey("DSM_STRING", DefaultLanguageHighlighterColors.STRING)
        val UUID_LITERAL = createTextAttributesKey("DSM_UUID", DefaultLanguageHighlighterColors.STRING)
        val ENUM_VALUE = createTextAttributesKey("DSM_ENUM_VALUE", DefaultLanguageHighlighterColors.INSTANCE_FIELD)

        // Comments
        val LINE_COMMENT = createTextAttributesKey("DSM_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val DOC_COMMENT = createTextAttributesKey("DSM_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT)

        // Operators and symbols
        val BRACES = createTextAttributesKey("DSM_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val BRACKETS = createTextAttributesKey("DSM_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        val PARENTHESES = createTextAttributesKey("DSM_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
        val ANGLE_BRACKETS = createTextAttributesKey("DSM_ANGLE_BRACKETS", DefaultLanguageHighlighterColors.PARENTHESES)
        val COMMA = createTextAttributesKey("DSM_COMMA", DefaultLanguageHighlighterColors.COMMA)
        val SEMICOLON = createTextAttributesKey("DSM_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val DOT = createTextAttributesKey("DSM_DOT", DefaultLanguageHighlighterColors.DOT)
        val OPERATOR = createTextAttributesKey("DSM_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)

        // Bad character
        val BAD_CHARACTER = createTextAttributesKey("DSM_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        // Arrays for efficient lookup
        private val KEYWORD_KEYS = arrayOf(KEYWORD)
        private val TYPE_KEYWORD_KEYS = arrayOf(TYPE_KEYWORD)
        private val IDENTIFIER_KEYS = arrayOf(IDENTIFIER)
        private val NUMBER_KEYS = arrayOf(NUMBER)
        private val STRING_KEYS = arrayOf(STRING)
        private val UUID_KEYS = arrayOf(UUID_LITERAL)
        private val ENUM_VALUE_KEYS = arrayOf(ENUM_VALUE)
        private val LINE_COMMENT_KEYS = arrayOf(LINE_COMMENT)
        private val DOC_COMMENT_KEYS = arrayOf(DOC_COMMENT)
        private val BRACES_KEYS = arrayOf(BRACES)
        private val BRACKETS_KEYS = arrayOf(BRACKETS)
        private val PARENTHESES_KEYS = arrayOf(PARENTHESES)
        private val ANGLE_BRACKETS_KEYS = arrayOf(ANGLE_BRACKETS)
        private val COMMA_KEYS = arrayOf(COMMA)
        private val SEMICOLON_KEYS = arrayOf(SEMICOLON)
        private val DOT_KEYS = arrayOf(DOT)
        private val OPERATOR_KEYS = arrayOf(OPERATOR)
        private val BAD_CHARACTER_KEYS = arrayOf(BAD_CHARACTER)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }

    override fun getHighlightingLexer(): Lexer = DSMLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            // Keywords
            DSMTypes.NAMESPACE, DSMTypes.CONCEPT, DSMTypes.STRUCT, DSMTypes.ENUM,
            DSMTypes.ATTACHMENT, DSMTypes.CLUB, DSMTypes.MEMBERSHIP,
            DSMTypes.FUNCTION_POOL, DSMTypes.ATTACHMENT_FUNCTION_POOL,
            DSMTypes.MUTABLE, DSMTypes.IS_A,
            DSMTypes.TRUE, DSMTypes.FALSE -> KEYWORD_KEYS

            // Primitive types
            DSMTypes.VOID, DSMTypes.BOOL,
            DSMTypes.INT8, DSMTypes.INT16, DSMTypes.INT32, DSMTypes.INT64,
            DSMTypes.UINT8, DSMTypes.UINT16, DSMTypes.UINT32, DSMTypes.UINT64,
            DSMTypes.FLOAT, DSMTypes.DOUBLE,
            DSMTypes.STRING, DSMTypes.UUID_TYPE, DSMTypes.BLOB_ID, DSMTypes.COMMIT_ID,
            DSMTypes.ANY, DSMTypes.ANY_CONCEPT -> TYPE_KEYWORD_KEYS

            // Generic/container types
            DSMTypes.KEY, DSMTypes.OPTIONAL, DSMTypes.VECTOR, DSMTypes.SET,
            DSMTypes.MAP, DSMTypes.TUPLE, DSMTypes.VARIANT,
            DSMTypes.XARRAY, DSMTypes.VEC, DSMTypes.MAT -> TYPE_KEYWORD_KEYS

            // Identifiers
            DSMTypes.IDENTIFIER -> IDENTIFIER_KEYS

            // Literals
            DSMTypes.NUMBER -> NUMBER_KEYS
            DSMTypes.STRING_LITERAL -> STRING_KEYS
            DSMTypes.UUID -> UUID_KEYS
            DSMTypes.ENUM_VALUE -> ENUM_VALUE_KEYS

            // Comments
            DSMTypes.LINE_COMMENT -> LINE_COMMENT_KEYS
            DSMTypes.DOC_STRING -> DOC_COMMENT_KEYS

            // Braces, brackets, parentheses
            DSMTypes.LBRACE, DSMTypes.RBRACE -> BRACES_KEYS
            DSMTypes.LBRACKET, DSMTypes.RBRACKET -> BRACKETS_KEYS
            DSMTypes.LPAREN, DSMTypes.RPAREN -> PARENTHESES_KEYS
            DSMTypes.LANGLE, DSMTypes.RANGLE -> ANGLE_BRACKETS_KEYS

            // Symbols
            DSMTypes.COMMA -> COMMA_KEYS
            DSMTypes.SEMICOLON -> SEMICOLON_KEYS
            DSMTypes.DOT -> DOT_KEYS
            DSMTypes.EQ, DSMTypes.COLON -> OPERATOR_KEYS

            // Bad character
            TokenType.BAD_CHARACTER -> BAD_CHARACTER_KEYS

            else -> EMPTY_KEYS
        }
    }
}
