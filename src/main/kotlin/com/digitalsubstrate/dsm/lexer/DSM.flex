package com.digitalsubstrate.dsm.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.digitalsubstrate.dsm.psi.DSMTypes;
import com.intellij.psi.TokenType;

%%

%class DSMLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

// Whitespace
WHITE_SPACE=[\ \t\f\r\n]+

// Comments
LINE_COMMENT="//"[^\r\n]*

// Docstrings (not comments - structured metadata preserved in PSI)
DOC_STRING=\"\"\"~\"\"\"

// Identifiers
IDENTIFIER=[a-zA-Z_][a-zA-Z0-9_]*(::[a-zA-Z_][a-zA-Z0-9_]*)?

// Literals
NUMBER=-?[0-9]+(\.[0-9]+)?([eE][+-]?[0-9]+)?
STRING=\"([^\"\\]|\\.)*\"
UUID="{"[0-9a-fA-F]{8}"-"[0-9a-fA-F]{4}"-"[0-9a-fA-F]{4}"-"[0-9a-fA-F]{4}"-"[0-9a-fA-F]{12}"}"
ENUM_VALUE=\.[a-zA-Z_][a-zA-Z0-9_]*

%%

<YYINITIAL> {
    // Docstrings (must come first to match before other tokens)
    {DOC_STRING}        { return DSMTypes.DOC_STRING; }

    // Comments
    {LINE_COMMENT}      { return DSMTypes.LINE_COMMENT; }

    // Keywords
    "namespace"             { return DSMTypes.NAMESPACE; }
    "concept"               { return DSMTypes.CONCEPT; }
    "struct"                { return DSMTypes.STRUCT; }
    "enum"                  { return DSMTypes.ENUM; }
    "attachment"            { return DSMTypes.ATTACHMENT; }
    "club"                  { return DSMTypes.CLUB; }
    "membership"            { return DSMTypes.MEMBERSHIP; }
    "function_pool"         { return DSMTypes.FUNCTION_POOL; }
    "attachment_function_pool"  { return DSMTypes.ATTACHMENT_FUNCTION_POOL; }
    "mutable"               { return DSMTypes.MUTABLE; }
    "is" {WHITE_SPACE}+ "a" { return DSMTypes.IS_A; }

    // Primitive types
    "void"      { return DSMTypes.VOID; }
    "bool"      { return DSMTypes.BOOL; }
    "int8"      { return DSMTypes.INT8; }
    "int16"     { return DSMTypes.INT16; }
    "int32"     { return DSMTypes.INT32; }
    "int64"     { return DSMTypes.INT64; }
    "uint8"     { return DSMTypes.UINT8; }
    "uint16"    { return DSMTypes.UINT16; }
    "uint32"    { return DSMTypes.UINT32; }
    "uint64"    { return DSMTypes.UINT64; }
    "float"     { return DSMTypes.FLOAT; }
    "double"    { return DSMTypes.DOUBLE; }
    "string"    { return DSMTypes.STRING; }
    "uuid"      { return DSMTypes.UUID_TYPE; }
    "blob_id"   { return DSMTypes.BLOB_ID; }
    "commit_id" { return DSMTypes.COMMIT_ID; }
    "any"       { return DSMTypes.ANY; }
    "any_concept" { return DSMTypes.ANY_CONCEPT; }

    // Generic/container types
    "key"       { return DSMTypes.KEY; }
    "optional"  { return DSMTypes.OPTIONAL; }
    "vector"    { return DSMTypes.VECTOR; }
    "set"       { return DSMTypes.SET; }
    "map"       { return DSMTypes.MAP; }
    "tuple"     { return DSMTypes.TUPLE; }
    "variant"   { return DSMTypes.VARIANT; }
    "xarray"    { return DSMTypes.XARRAY; }
    "vec"       { return DSMTypes.VEC; }
    "mat"       { return DSMTypes.MAT; }

    // Boolean literals
    "true"      { return DSMTypes.TRUE; }
    "false"     { return DSMTypes.FALSE; }

    // Literals (must come BEFORE symbols to match UUID before '{')
    {UUID}              { return DSMTypes.UUID; }
    {STRING}            { return DSMTypes.STRING_LITERAL; }
    {NUMBER}            { return DSMTypes.NUMBER; }
    {ENUM_VALUE}        { return DSMTypes.ENUM_VALUE; }

    // Symbols
    "{"         { return DSMTypes.LBRACE; }
    "}"         { return DSMTypes.RBRACE; }
    "("         { return DSMTypes.LPAREN; }
    ")"         { return DSMTypes.RPAREN; }
    "<"         { return DSMTypes.LANGLE; }
    ">"         { return DSMTypes.RANGLE; }
    "["         { return DSMTypes.LBRACKET; }
    "]"         { return DSMTypes.RBRACKET; }
    ";"         { return DSMTypes.SEMICOLON; }
    ","         { return DSMTypes.COMMA; }
    "."         { return DSMTypes.DOT; }
    ":"         { return DSMTypes.COLON; }
    "="         { return DSMTypes.EQ; }

    // Identifier (after keywords and literals)
    {IDENTIFIER}        { return DSMTypes.IDENTIFIER; }
    {WHITE_SPACE}       { return TokenType.WHITE_SPACE; }

    // Catch-all for unknown characters
    [^]                 { return TokenType.BAD_CHARACTER; }
}
