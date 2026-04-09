package com.digitalsubstrate.dsm.liveTemplates

import com.digitalsubstrate.dsm.core.DSMLanguage
import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

@Suppress("DEPRECATION")
class DSMTemplateContextType : TemplateContextType("DSM", "DSM") {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        return templateActionContext.file.language == DSMLanguage.INSTANCE
    }
}
