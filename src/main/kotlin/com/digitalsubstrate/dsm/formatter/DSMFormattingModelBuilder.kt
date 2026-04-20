// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.formatter

import com.digitalsubstrate.dsm.psi.DSMTypes
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * Formatting model builder for DSM language.
 *
 * Defines how DSM code should be formatted, including:
 * - Indentation rules for namespaces, concepts, structs, enums, etc.
 * - Spacing rules around braces, semicolons, operators
 * - Line breaks and wrapping behavior
 */
class DSMFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val codeStyleSettings = formattingContext.codeStyleSettings
        val element = formattingContext.psiElement
        val containingFile = formattingContext.containingFile

        val spacingBuilder = createSpacingBuilder(codeStyleSettings)

        val rootBlock = DSMBlock(
            node = formattingContext.node,
            wrap = null,
            alignment = null,
            spacingBuilder = spacingBuilder
        )

        return FormattingModelProvider.createFormattingModelForPsiFile(
            containingFile,
            rootBlock,
            codeStyleSettings
        )
    }

    /**
     * Creates spacing rules for DSM syntax elements.
     *
     * Defines spaces around:
     * - Braces: namespace MyApp { ... }
     * - Semicolons: concept Person;
     * - Operators: is a, type parameters
     * - Commas: parameter lists, type lists
     */
    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        return SpacingBuilder(settings, com.digitalsubstrate.dsm.core.DSMLanguage.INSTANCE)
            // Braces for declarations - line breaks (but NOT for list literals)
            .before(DSMTypes.LBRACE).spaces(1)              // namespace MyApp {
            .afterInside(DSMTypes.LBRACE, DSMTypes.NAMESPACE_BODY).lineBreakInCode()
            .afterInside(DSMTypes.LBRACE, DSMTypes.STRUCT_DECLARATION).lineBreakInCode()
            .afterInside(DSMTypes.LBRACE, DSMTypes.ENUM_DECLARATION).lineBreakInCode()
            .afterInside(DSMTypes.LBRACE, DSMTypes.FUNCTION_POOL_DECLARATION).lineBreakInCode()
            .afterInside(DSMTypes.LBRACE, DSMTypes.ATTACHMENT_FUNCTION_POOL_DECLARATION).lineBreakInCode()
            .beforeInside(DSMTypes.RBRACE, DSMTypes.NAMESPACE_BODY).lineBreakInCode()
            .beforeInside(DSMTypes.RBRACE, DSMTypes.STRUCT_DECLARATION).lineBreakInCode()
            .beforeInside(DSMTypes.RBRACE, DSMTypes.ENUM_DECLARATION).lineBreakInCode()
            .beforeInside(DSMTypes.RBRACE, DSMTypes.FUNCTION_POOL_DECLARATION).lineBreakInCode()
            .beforeInside(DSMTypes.RBRACE, DSMTypes.ATTACHMENT_FUNCTION_POOL_DECLARATION).lineBreakInCode()

            // List literals - no line breaks, spaces inside
            .afterInside(DSMTypes.LBRACE, DSMTypes.LIST_LITERAL).spaces(0)  // {0.0
            .beforeInside(DSMTypes.RBRACE, DSMTypes.LIST_LITERAL).spaces(0) // 0.0}

            // Semicolons - NEVER line break before semicolon
            .before(DSMTypes.SEMICOLON).spaces(0)           // concept Person; or };
            .after(DSMTypes.SEMICOLON).lineBreakInCode()    // ; \n

            // Angle brackets (generics)
            .before(DSMTypes.LANGLE).spaces(0)              // vector<T>
            .after(DSMTypes.LANGLE).spaces(0)               // <T>
            .before(DSMTypes.RANGLE).spaces(0)              // <T>

            // Space after > except when followed by another > (nested generics)
            .between(DSMTypes.RANGLE, DSMTypes.RANGLE).spaces(0)  // >> (optional<key<Person>>)
            .after(DSMTypes.RANGLE).spaces(1)               // <T> field or attachment<A, B> name

            // Parentheses (function parameters)
            .before(DSMTypes.LPAREN).spaces(0)              // function(
            .after(DSMTypes.LPAREN).spaces(0)               // (param
            .before(DSMTypes.RPAREN).spaces(0)              // param)
            .after(DSMTypes.RPAREN).spaces(0)               // );

            // Square brackets (type syntax)
            .before(DSMTypes.LBRACKET).spaces(0)            // array[
            .after(DSMTypes.LBRACKET).spaces(0)             // [index
            .before(DSMTypes.RBRACKET).spaces(0)            // index]
            .after(DSMTypes.RBRACKET).spaces(0)             // ]

            // Commas
            .before(DSMTypes.COMMA).spaces(0)               // a, b
            .after(DSMTypes.COMMA).spaces(1)                // a, b

            // Colon (type annotations)
            .before(DSMTypes.COLON).spaces(0)               // namespace MyApp :
            .after(DSMTypes.COLON).spaces(1)                // : type

            // Equals (default values)
            .before(DSMTypes.EQ).spaces(1)                  // field = value
            .after(DSMTypes.EQ).spaces(1)                   // = value

            // Keywords with identifiers
            .after(DSMTypes.NAMESPACE).spaces(1)            // namespace MyApp
            .after(DSMTypes.CONCEPT).spaces(1)              // concept Person
            .after(DSMTypes.STRUCT).spaces(1)               // struct Data
            .after(DSMTypes.ENUM).spaces(1)                 // enum Status
            .after(DSMTypes.CLUB).spaces(1)                 // club Group
            .after(DSMTypes.ATTACHMENT).spaces(0)           // attachment<
            .after(DSMTypes.MEMBERSHIP).spaces(1)           // membership Person
            .after(DSMTypes.FUNCTION_POOL).spaces(1)        // function_pool Tools
            .after(DSMTypes.ATTACHMENT_FUNCTION_POOL).spaces(1) // attachment_function_pool Model

            // 'is a' inheritance syntax
            .after(DSMTypes.IS_A).spaces(1)                 // is a Base

            // Mutable keyword
            .after(DSMTypes.MUTABLE).spaces(1)              // mutable void
    }

    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? {
        return null
    }
}
