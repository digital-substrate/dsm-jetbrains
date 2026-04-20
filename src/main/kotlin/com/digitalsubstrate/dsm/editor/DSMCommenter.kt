// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.editor

import com.intellij.lang.Commenter

/**
 * Provides line and block commenting functionality for DSM files.
 * Enables Cmd+/ (line comment) and Cmd+Shift+/ (block comment) shortcuts.
 */
class DSMCommenter : Commenter {

    /**
     * Line comment prefix: //
     */
    override fun getLineCommentPrefix(): String? = "//"

    /**
     * Block comment start - DSM doesn't support block comments, return null
     * This makes Cmd+Shift+/ use line comments for each line instead
     */
    override fun getBlockCommentPrefix(): String? = null

    /**
     * Block comment end - not used in DSM
     */
    override fun getBlockCommentSuffix(): String? = null

    /**
     * Return null - DSM doesn't use commented block prefixes
     */
    override fun getCommentedBlockCommentPrefix(): String? = null

    /**
     * Return null - DSM doesn't use commented block suffixes
     */
    override fun getCommentedBlockCommentSuffix(): String? = null
}
