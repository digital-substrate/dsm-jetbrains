package com.digitalsubstrate.dsm.core

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object DSMIcons {
    @JvmField
    val FILE: Icon = IconLoader.getIcon("/icons/dsm.svg", DSMIcons::class.java)

    @JvmField
    val CONCEPT: Icon = FILE

    @JvmField
    val STRUCT: Icon = FILE

    @JvmField
    val ENUM: Icon = FILE

    @JvmField
    val CLUB: Icon = FILE

    @JvmField
    val ATTACHMENT: Icon = FILE

    @JvmField
    val FUNCTION_POOL: Icon = FILE

    @JvmField
    val NAMESPACE: Icon = FILE
}
