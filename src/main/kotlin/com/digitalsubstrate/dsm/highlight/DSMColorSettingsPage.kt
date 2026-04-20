// Copyright (c) Digital Substrate 2026, All rights reserved. MIT License.
package com.digitalsubstrate.dsm.highlight

import com.digitalsubstrate.dsm.core.DSMIcons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class DSMColorSettingsPage : ColorSettingsPage {

    companion object {
        private val DESCRIPTORS = arrayOf(
            // Syntax Highlighting
            AttributesDescriptor("Keyword", DSMSyntaxHighlighter.KEYWORD),
            AttributesDescriptor("Type Keyword", DSMSyntaxHighlighter.TYPE_KEYWORD),
            AttributesDescriptor("Identifier", DSMSyntaxHighlighter.IDENTIFIER),
            AttributesDescriptor("Number", DSMSyntaxHighlighter.NUMBER),
            AttributesDescriptor("String", DSMSyntaxHighlighter.STRING),
            AttributesDescriptor("UUID Literal", DSMSyntaxHighlighter.UUID_LITERAL),
            AttributesDescriptor("Enum Value", DSMSyntaxHighlighter.ENUM_VALUE),
            AttributesDescriptor("Line Comment", DSMSyntaxHighlighter.LINE_COMMENT),
            AttributesDescriptor("Doc Comment", DSMSyntaxHighlighter.DOC_COMMENT),
            AttributesDescriptor("Braces", DSMSyntaxHighlighter.BRACES),
            AttributesDescriptor("Brackets", DSMSyntaxHighlighter.BRACKETS),
            AttributesDescriptor("Parentheses", DSMSyntaxHighlighter.PARENTHESES),
            AttributesDescriptor("Angle Brackets", DSMSyntaxHighlighter.ANGLE_BRACKETS),
            AttributesDescriptor("Comma", DSMSyntaxHighlighter.COMMA),
            AttributesDescriptor("Semicolon", DSMSyntaxHighlighter.SEMICOLON),
            AttributesDescriptor("Dot", DSMSyntaxHighlighter.DOT),
            AttributesDescriptor("Operator", DSMSyntaxHighlighter.OPERATOR),

            // Semantic Highlighting - Definitions
            AttributesDescriptor("Semantic//Concept Definition", DSMSemanticHighlightingAnnotator.CONCEPT_DEFINITION),
            AttributesDescriptor("Semantic//Struct Definition", DSMSemanticHighlightingAnnotator.STRUCT_DEFINITION),
            AttributesDescriptor("Semantic//Enum Definition", DSMSemanticHighlightingAnnotator.ENUM_DEFINITION),
            AttributesDescriptor("Semantic//Club Definition", DSMSemanticHighlightingAnnotator.CLUB_DEFINITION),
            AttributesDescriptor("Semantic//Function Pool Definition", DSMSemanticHighlightingAnnotator.FUNCTION_POOL_DEFINITION),

            // Semantic Highlighting - Members
            AttributesDescriptor("Semantic//Struct Field", DSMSemanticHighlightingAnnotator.STRUCT_FIELD),
            AttributesDescriptor("Semantic//Mutable Field", DSMSemanticHighlightingAnnotator.MUTABLE_FIELD),
            AttributesDescriptor("Semantic//Enum Case", DSMSemanticHighlightingAnnotator.ENUM_CASE),
            AttributesDescriptor("Semantic//Function Declaration", DSMSemanticHighlightingAnnotator.FUNCTION_DECLARATION),
            AttributesDescriptor("Semantic//Parameter", DSMSemanticHighlightingAnnotator.PARAMETER),

            // Semantic Highlighting - Type References
            AttributesDescriptor("Semantic//Concept Reference", DSMSemanticHighlightingAnnotator.CONCEPT_REFERENCE),
            AttributesDescriptor("Semantic//Struct Reference", DSMSemanticHighlightingAnnotator.STRUCT_REFERENCE),
            AttributesDescriptor("Semantic//Enum Reference", DSMSemanticHighlightingAnnotator.ENUM_REFERENCE),
            AttributesDescriptor("Semantic//Club Reference", DSMSemanticHighlightingAnnotator.CLUB_REFERENCE),

            // Semantic Highlighting - Other
            AttributesDescriptor("Semantic//Attachment Key", DSMSemanticHighlightingAnnotator.ATTACHMENT_KEY),
        )

        private val DEMO_TEXT = // language=DSM
            """// Sample DSM file demonstrating syntax highlighting.
namespace SampleNamespace {a1b2c3d4-e5f6-7890-abcd-ef1234567890} {

    // Concept with inheritance
    concept Vehicle;
    concept Car is a Vehicle;

    // Data structure for a person.
    struct Person {
        string name;
        uint32 age = 0;
        bool active = true;
        optional<string> email;
        vec<float, 3> position = {0.0, 0.0, 0.0};
    };

    // Status enumeration.
    enum Status {
        active,
        inactive,
        pending
    };

    // Club and membership
    club Entity;
    membership Entity Vehicle;

    // Attaches person data to Vehicle concept.
    attachment<Vehicle, Person> owner;
};

// Utility functions.
function_pool Utils {12345678-90ab-cdef-1234-567890abcdef} {
    int64 add(int64 a, int64 b);
    bool isValid(string value);
};

// Model management operations.
attachment_function_pool Model {fedcba98-7654-3210-fedc-ba9876543210} {
    mutable key<Vehicle> createVehicle(string model);
    set<key<Vehicle>> getAllVehicles();
};
"""
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "DSM"

    override fun getIcon(): Icon = DSMIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = DSMSyntaxHighlighter()

    override fun getDemoText(): String = DEMO_TEXT

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null
}
