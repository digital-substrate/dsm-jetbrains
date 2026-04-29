// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.liveTemplates

import com.digitalsubstrate.dsm.core.DSMLanguage
import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

class DSMTemplateContextType : TemplateContextType("DSM") {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        return templateActionContext.file.language == DSMLanguage.INSTANCE
    }
}
