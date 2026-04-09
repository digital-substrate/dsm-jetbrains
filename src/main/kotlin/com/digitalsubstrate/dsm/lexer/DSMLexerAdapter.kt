package com.digitalsubstrate.dsm.lexer

import com.intellij.lexer.FlexAdapter

class DSMLexerAdapter : FlexAdapter(DSMLexer(null))
