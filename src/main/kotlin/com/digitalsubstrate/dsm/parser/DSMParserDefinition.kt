package com.digitalsubstrate.dsm.parser

import com.digitalsubstrate.dsm.core.DSMLanguage
import com.digitalsubstrate.dsm.lexer.DSMLexerAdapter
import com.digitalsubstrate.dsm.psi.DSMFile
import com.digitalsubstrate.dsm.psi.DSMTypes
import com.digitalsubstrate.dsm.stub.DSMFileStub
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class DSMParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = DSMLexerAdapter()

    override fun createParser(project: Project?): PsiParser = DSMParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRINGS

    override fun createElement(node: ASTNode): PsiElement = DSMTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = DSMFile(viewProvider)

    companion object {
        val FILE = DSMFileStub.Type

        val COMMENTS = TokenSet.create(
            DSMTypes.LINE_COMMENT,
            DSMTypes.DOC_STRING  // Kept in COMMENTS for parser, but extracted via PsiTreeUtil
        )

        val STRINGS = TokenSet.create(
            DSMTypes.STRING_LITERAL
        )
    }
}
