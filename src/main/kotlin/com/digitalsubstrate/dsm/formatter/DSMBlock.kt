// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.formatter

import com.digitalsubstrate.dsm.psi.DSMTypes
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock

/**
 * Formatting block for DSM language elements.
 *
 * Defines the indentation and spacing behavior for DSM syntax elements.
 * Each block corresponds to a PSI element and defines how it and its children should be formatted.
 */
class DSMBlock(
    private val node: ASTNode,
    private val wrap: Wrap?,
    private val alignment: Alignment?,
    private val spacingBuilder: SpacingBuilder
) : AbstractBlock(node, wrap, alignment) {

    /**
     * Builds child blocks for formatting.
     *
     * Creates a block for each child node, determining indentation and alignment.
     */
    override fun buildChildren(): List<Block> {
        val blocks = mutableListOf<Block>()
        var child = node.firstChildNode

        while (child != null) {
            // Skip whitespace and comments (they're handled by spacing rules)
            if (child.elementType != TokenType.WHITE_SPACE &&
                child.textLength > 0) {

                val childBlock = DSMBlock(
                    node = child,
                    wrap = null,
                    alignment = null,
                    spacingBuilder = spacingBuilder
                )
                blocks.add(childBlock)
            }
            child = child.treeNext
        }

        return blocks
    }

    /**
     * Determines indentation for this block.
     *
     * DSM indentation rules:
     * - Namespace body: indent children
     * - Concept/Struct/Enum/Club declarations: no indent (top-level)
     * - Fields in structs: indent
     * - Function pool body: indent children
     * - Functions in pools: indent
     * - Parameters: continuation indent
     */
    override fun getIndent(): Indent? {
        val parent = node.treeParent
        val parentType = parent?.elementType
        val nodeType = node.elementType

        // Special handling for braces
        if (nodeType == DSMTypes.LBRACE || nodeType == DSMTypes.RBRACE) {
            return Indent.getNoneIndent()
        }

        // Indentation rules based on parent context
        return when (parentType) {
            // Children of namespace body should NOT be indented
            DSMTypes.NAMESPACE_BODY -> {
                Indent.getNoneIndent()
            }

            // Children of struct declaration
            DSMTypes.STRUCT_DECLARATION -> {
                when (nodeType) {
                    DSMTypes.STRUCT,
                    DSMTypes.IDENTIFIER,
                    DSMTypes.LBRACE,
                    DSMTypes.RBRACE -> Indent.getNoneIndent()
                    DSMTypes.FIELD_DECLARATION -> Indent.getNormalIndent()
                    else -> Indent.getNormalIndent()
                }
            }

            // Children of enum declaration
            DSMTypes.ENUM_DECLARATION -> {
                when (nodeType) {
                    DSMTypes.ENUM,
                    DSMTypes.IDENTIFIER,
                    DSMTypes.LBRACE,
                    DSMTypes.RBRACE -> Indent.getNoneIndent()
                    DSMTypes.ENUM_VALUE_LIST,
                    DSMTypes.ENUM_VALUE_DECL -> Indent.getNormalIndent()
                    else -> Indent.getNormalIndent()
                }
            }

            // Children of function pool - NO indentation
            DSMTypes.FUNCTION_POOL_DECLARATION,
            DSMTypes.ATTACHMENT_FUNCTION_POOL_DECLARATION -> {
                Indent.getNoneIndent()
            }

            // Children of function declarations
            DSMTypes.FUNCTION_DECLARATION,
            DSMTypes.ATTACHMENT_FUNCTION_DECLARATION -> {
                when (nodeType) {
                    DSMTypes.MUTABLE,
                    DSMTypes.IDENTIFIER,
                    DSMTypes.TYPE_REFERENCE,
                    DSMTypes.PARAMETER_LIST,
                    DSMTypes.LPAREN,
                    DSMTypes.RPAREN,
                    DSMTypes.SEMICOLON -> Indent.getNoneIndent()
                    else -> Indent.getNoneIndent()
                }
            }

            // Children of parameter list - use continuation indent for wrapped parameters
            DSMTypes.PARAMETER_LIST -> {
                when (nodeType) {
                    DSMTypes.LPAREN, DSMTypes.RPAREN -> Indent.getNoneIndent()
                    DSMTypes.PARAMETER -> Indent.getContinuationIndent()
                    else -> Indent.getContinuationIndent()
                }
            }

            // Children of enum value list
            DSMTypes.ENUM_VALUE_LIST -> {
                when (nodeType) {
                    DSMTypes.ENUM_VALUE_DECL -> Indent.getNoneIndent()
                    else -> Indent.getNoneIndent()
                }
            }

            // Top-level elements in file
            null -> {
                when (nodeType) {
                    DSMTypes.NAMESPACE_DECLARATION -> Indent.getNoneIndent()
                    else -> Indent.getNoneIndent()
                }
            }

            // Default: no indent
            else -> Indent.getNoneIndent()
        }
    }

    /**
     * Determines spacing between child blocks.
     */
    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder.getSpacing(this, child1, child2)
    }

    /**
     * Indicates if this block is a leaf (has no children).
     */
    override fun isLeaf(): Boolean {
        return node.firstChildNode == null
    }

    /**
     * Child indent determines how children of this block should be indented.
     */
    override fun getChildIndent(): Indent? {
        return when (node.elementType) {
            // Only struct and enum have indented children
            DSMTypes.STRUCT_DECLARATION,
            DSMTypes.ENUM_DECLARATION -> Indent.getNormalIndent()

            DSMTypes.PARAMETER_LIST -> Indent.getContinuationIndent()

            // Namespace, function pools: no indentation
            DSMTypes.NAMESPACE_BODY,
            DSMTypes.FUNCTION_POOL_DECLARATION,
            DSMTypes.ATTACHMENT_FUNCTION_POOL_DECLARATION -> Indent.getNoneIndent()

            else -> Indent.getNoneIndent()
        }
    }
}
