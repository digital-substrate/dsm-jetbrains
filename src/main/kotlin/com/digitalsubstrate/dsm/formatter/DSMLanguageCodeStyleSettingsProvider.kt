// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.formatter

import com.digitalsubstrate.dsm.core.DSMLanguage
import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

/**
 * Code style settings provider for DSM language.
 *
 * Defines default indentation settings and provides UI for customization.
 */
class DSMLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage(): Language = DSMLanguage.INSTANCE

    override fun getCodeSample(settingsType: SettingsType): String {
        return "namespace MyApp {12345678-1234-1234-1234-123456789abc} {\n" +
            "concept Person;\n" +
            "concept Employee is a Person;\n" +
            "\n" +
            "struct PersonData {\n" +
            "    string name;\n" +
            "    uint32 age;\n" +
            "    optional<string> email;\n" +
            "    vec<float, 3> position = {0.0, 0.0, 0.0};\n" +
            "};\n" +
            "\n" +
            "enum Status {\n" +
            "    active,\n" +
            "    inactive,\n" +
            "    pending\n" +
            "};\n" +
            "\n" +
            "attachment<Person, PersonData> data;\n" +
            "};\n" +
            "function_pool Tools {dc9740c9-9d1d-4c1e-9caa-4c8843b91e82} {\n" +
            "int64 add(int64 a, int64 b);\n" +
            "bool isEven(int64 n);\n" +
            "};\n" +
            "\n" +
            "attachment_function_pool Model {9bdcbb5b-76e9-426f-b8a6-a10ed2d949e6} {\n" +
            "mutable key<Person> createPerson(\n" +
            "    string name,\n" +
            "    uint32 age\n" +
            ");\n" +
            "optional<key<Person>> findByName(string name);\n" +
            "};"
    }

    /**
     * Customizes code style settings specific to DSM.
     */
    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {
            SettingsType.SPACING_SETTINGS -> {
                consumer.showStandardOptions(
                    "SPACE_BEFORE_METHOD_CALL_PARENTHESES",
                    "SPACE_BEFORE_METHOD_PARENTHESES",
                    "SPACE_AROUND_ASSIGNMENT_OPERATORS",
                    "SPACE_AFTER_COMMA",
                    "SPACE_BEFORE_COMMA"
                )
            }
            SettingsType.INDENT_SETTINGS -> {
                consumer.showStandardOptions(
                    "INDENT_SIZE",
                    "CONTINUATION_INDENT_SIZE",
                    "TAB_SIZE",
                    "USE_TAB_CHARACTER"
                )
            }
            SettingsType.WRAPPING_AND_BRACES_SETTINGS -> {
                consumer.showStandardOptions(
                    "KEEP_LINE_BREAKS",
                    "KEEP_BLANK_LINES_IN_CODE"
                )
            }
            SettingsType.BLANK_LINES_SETTINGS -> {
                consumer.showStandardOptions(
                    "KEEP_BLANK_LINES_IN_CODE"
                )
            }
            else -> {}
        }
    }

    /**
     * Returns the indent options editor for DSM.
     */
    override fun getIndentOptionsEditor(): IndentOptionsEditor {
        return SmartIndentOptionsEditor()
    }

    /**
     * Sets default common code style settings for DSM.
     */
    override fun customizeDefaults(
        commonSettings: CommonCodeStyleSettings,
        indentOptions: CommonCodeStyleSettings.IndentOptions
    ) {
        // Indentation defaults
        indentOptions.INDENT_SIZE = 4
        indentOptions.CONTINUATION_INDENT_SIZE = 4
        indentOptions.TAB_SIZE = 4
        indentOptions.USE_TAB_CHARACTER = false

        // Spacing defaults
        commonSettings.SPACE_BEFORE_METHOD_CALL_PARENTHESES = false
        commonSettings.SPACE_BEFORE_METHOD_PARENTHESES = false
        commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS = true
        commonSettings.SPACE_AFTER_COMMA = true
        commonSettings.SPACE_BEFORE_COMMA = false

        // Line breaks
        commonSettings.KEEP_LINE_BREAKS = true
        commonSettings.KEEP_BLANK_LINES_IN_CODE = 1
    }

    /**
     * Returns the default file extension for DSM files.
     */
    override fun getFileExt(): String = "dsm"

    /**
     * Returns the display name for DSM language in settings.
     */
    override fun getLanguageName(): String = "DSM"
}
