package com.digitalsubstrate.dsm.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType

/**
 * PSI utilities for DSM language elements.
 *
 * These utilities follow IntelliJ Platform best practices by navigating
 * the PSI/AST tree rather than scanning source text with regex.
 */
object DSMPsiUtil {
    /**
     * Extracts the docstring associated with a PSI element.
     *
     * Searches backwards through previous siblings to find a DOC_STRING token.
     * Stops if it encounters non-whitespace/non-comment code, indicating
     * there's no docstring for this element.
     *
     * **Implementation note:**
     * DOC_STRING is in the COMMENTS TokenSet (see DSMParserDefinition),
     * which means it's filtered from the parser's view. However, the token
     * remains in the AST as a sibling, not a child. This is why we use
     * `treePrev` navigation instead of `findChildByType()`.
     *
     * @param element The PSI element to find documentation for
     * @return The docstring text (without triple-quotes), or null if none found
     */
    @JvmStatic
    fun getDocString(element: PsiElement): String? {
        // Search in previous siblings
        var node = element.node.treePrev

        while (node != null) {
            when (node.elementType) {
                DSMTypes.DOC_STRING -> {
                    return node.text.removeSurrounding("\"\"\"").trim()
                }

                TokenType.WHITE_SPACE,
                DSMTypes.LINE_COMMENT -> {
                    // Skip whitespace and line comments - continue searching
                    node = node.treePrev
                }

                else -> {
                    // Encountered other code - stop sibling search
                    break
                }
            }
        }

        // Special case: first element in namespace body
        // The docstring is a sibling of the parent node (namespaceBody/etc.)
        if (element.node.treePrev == null) {
            val parent = element.parent?.node
            if (parent != null) {
                var parentPrev = parent.treePrev
                while (parentPrev != null) {
                    when (parentPrev.elementType) {
                        DSMTypes.DOC_STRING -> {
                            return parentPrev.text.removeSurrounding("\"\"\"").trim()
                        }
                        TokenType.WHITE_SPACE,
                        DSMTypes.LINE_COMMENT -> {
                            parentPrev = parentPrev.treePrev
                        }
                        else -> break
                    }
                }
            }
        }

        return null
    }
}
