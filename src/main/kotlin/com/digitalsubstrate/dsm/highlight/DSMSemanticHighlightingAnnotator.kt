package com.digitalsubstrate.dsm.highlight

import com.digitalsubstrate.dsm.psi.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import java.awt.Font

/**
 * Semantic highlighting annotator that applies context-aware syntax highlighting
 * based on the semantic role of DSM elements (not just their lexical token type).
 *
 * This provides visual distinction between:
 * - Different types of definitions (concept, struct, enum, club)
 * - Different identifier roles (field, parameter, local variable)
 * - Type references based on their definition type
 * - Special modifiers (mutable, keyfield)
 */
class DSMSemanticHighlightingAnnotator : Annotator {

    companion object {
        // Definition names (bold for declarations)
        val CONCEPT_DEFINITION = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_CONCEPT_DEF",
            DefaultLanguageHighlighterColors.CLASS_NAME
        )

        val STRUCT_DEFINITION = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_STRUCT_DEF",
            DefaultLanguageHighlighterColors.CLASS_NAME
        )

        val ENUM_DEFINITION = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_ENUM_DEF",
            DefaultLanguageHighlighterColors.CLASS_NAME
        )

        val CLUB_DEFINITION = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_CLUB_DEF",
            DefaultLanguageHighlighterColors.CLASS_NAME
        )

        val FUNCTION_POOL_DEFINITION = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_FUNCTION_POOL_DEF",
            DefaultLanguageHighlighterColors.CLASS_NAME
        )

        // Fields (italic style)
        val STRUCT_FIELD = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_STRUCT_FIELD",
            DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )

        val MUTABLE_FIELD = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_MUTABLE_FIELD",
            DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )

        // Enum values
        val ENUM_CASE = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_ENUM_CASE",
            DefaultLanguageHighlighterColors.STATIC_FIELD
        )

        // Functions
        val FUNCTION_DECLARATION = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_FUNCTION_DECL",
            DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
        )

        val FUNCTION_CALL = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_FUNCTION_CALL",
            DefaultLanguageHighlighterColors.FUNCTION_CALL
        )

        // Parameters
        val PARAMETER = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_PARAMETER",
            DefaultLanguageHighlighterColors.PARAMETER
        )

        // Type references (based on what they reference)
        // Semantic roles in DSM:
        // - Concepts & Clubs: Keys/references (same semantic role)
        // - Structs: Structured documents
        // - Enums: Enumerations

        val CONCEPT_REFERENCE = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_CONCEPT_REF",
            DefaultLanguageHighlighterColors.CLASS_REFERENCE  // Keys/references - standard class color
        )

        val CLUB_REFERENCE = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_CLUB_REF",
            DefaultLanguageHighlighterColors.INTERFACE_NAME  // Keys/references - similar to concepts but distinct
        )

        val STRUCT_REFERENCE = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_STRUCT_REF",
            DefaultLanguageHighlighterColors.INSTANCE_FIELD  // Structured documents - different semantic role
        )

        val ENUM_REFERENCE = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_ENUM_REF",
            DefaultLanguageHighlighterColors.STATIC_FIELD  // Enumerations - distinct from other types
        )

        // Attachments
        val ATTACHMENT_KEY = TextAttributesKey.createTextAttributesKey(
            "DSM_SEMANTIC_ATTACHMENT_KEY",
            DefaultLanguageHighlighterColors.METADATA
        )
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            // Definition names
            is DSMConceptDeclaration -> highlightDefinitionName(element, holder, CONCEPT_DEFINITION)
            is DSMStructDeclaration -> highlightDefinitionName(element, holder, STRUCT_DEFINITION)
            is DSMEnumDeclaration -> highlightDefinitionName(element, holder, ENUM_DEFINITION)
            is DSMClubDeclaration -> highlightDefinitionName(element, holder, CLUB_DEFINITION)
            is DSMFunctionPoolDeclaration -> highlightDefinitionName(element, holder, FUNCTION_POOL_DEFINITION)
            is DSMAttachmentFunctionPoolDeclaration -> highlightDefinitionName(element, holder, FUNCTION_POOL_DEFINITION)

            // Struct fields
            is DSMFieldDeclaration -> highlightField(element, holder)

            // Enum values
            is DSMEnumValueDecl -> highlightEnumCase(element, holder)

            // Function declarations (both types handled by same highlighter)
            is DSMFunctionDeclaration -> highlightFunction(element.identifier, holder)
            is DSMAttachmentFunctionDeclaration -> highlightFunction(element.identifier, holder)

            // Parameters
            is DSMParameter -> highlightParameter(element, holder)

            // Type references
            is DSMTypeReference -> highlightTypeReference(element, holder)

            // Attachment declarations
            is DSMAttachmentDeclaration -> highlightAttachment(element, holder)
        }
    }

    private fun highlightDefinitionName(
        element: PsiElement,
        holder: AnnotationHolder,
        attributesKey: TextAttributesKey
    ) {
        // Get the identifier element
        val identifier = when (element) {
            is DSMConceptDeclaration -> element.identifier
            is DSMStructDeclaration -> element.identifier
            is DSMEnumDeclaration -> element.identifier
            is DSMClubDeclaration -> element.identifier
            is DSMFunctionPoolDeclaration -> element.identifier
            is DSMAttachmentFunctionPoolDeclaration -> element.identifier
            else -> null
        }

        identifier?.let {
            // Create bold text attributes for definitions
            val baseAttrs = attributesKey.defaultAttributes ?: TextAttributes()
            val boldAttrs = baseAttrs.clone()
            boldAttrs.fontType = Font.BOLD

            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(it.textRange)
                .enforcedTextAttributes(boldAttrs)
                .create()
        }
    }

    private fun highlightField(element: DSMFieldDeclaration, holder: AnnotationHolder) {
        val identifier = element.identifierOrKeyword

        // Check if field is mutable (check for MUTABLE keyword in children)
        val isMutable = element.node.findChildByType(DSMTypes.MUTABLE) != null
        val attributesKey = if (isMutable) MUTABLE_FIELD else STRUCT_FIELD

        // Create italic text attributes for fields
        val baseAttrs = attributesKey.defaultAttributes ?: TextAttributes()
        val italicAttrs = baseAttrs.clone()
        italicAttrs.fontType = Font.ITALIC

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(identifier.textRange)
            .enforcedTextAttributes(italicAttrs)
            .create()
    }

    private fun highlightEnumCase(element: DSMEnumValueDecl, holder: AnnotationHolder) {
        val identifier = element.identifierOrKeyword

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(identifier.textRange)
            .textAttributes(ENUM_CASE)
            .create()
    }

    private fun highlightFunction(identifier: PsiElement?, holder: AnnotationHolder) {
        identifier ?: return

        // Create bold text attributes for function names
        val baseAttrs = FUNCTION_DECLARATION.defaultAttributes ?: TextAttributes()
        val boldAttrs = baseAttrs.clone()
        boldAttrs.fontType = Font.BOLD

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(identifier.textRange)
            .enforcedTextAttributes(boldAttrs)
            .create()
    }

    private fun highlightParameter(element: DSMParameter, holder: AnnotationHolder) {
        val identifier = element.identifierOrKeyword ?: return

        // Use enforced attributes to override syntax highlighting
        val baseAttrs = PARAMETER.defaultAttributes ?: TextAttributes()

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(identifier.textRange)
            .enforcedTextAttributes(baseAttrs)
            .create()
    }

    private fun highlightTypeReference(element: DSMTypeReference, holder: AnnotationHolder) {
        // Find ALL user type references in the type reference tree (including nested generics)
        val allUserTypeRefs = PsiTreeUtil.findChildrenOfType(element, DSMUserTypeReference::class.java)

        // Highlight each user type reference found
        allUserTypeRefs.forEach { userTypeRef ->
            val typeName = userTypeRef.text

            // Skip primitive/builtin types that are already well highlighted
            if (isBuiltinType(typeName)) {
                return@forEach
            }

            // Resolve the reference to find its declaration and determine type
            val resolved = userTypeRef.reference?.resolve()
            val attributesKey = when (resolved) {
                is DSMConceptDeclaration -> CONCEPT_REFERENCE
                is DSMEnumDeclaration -> ENUM_REFERENCE
                is DSMClubDeclaration -> CLUB_REFERENCE
                is DSMStructDeclaration -> STRUCT_REFERENCE
                else -> STRUCT_REFERENCE // Fallback if resolution fails
            }

            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(userTypeRef.identifier.textRange)
                .textAttributes(attributesKey)
                .create()
        }
    }

    private fun isBuiltinType(name: String): Boolean {
        return name in setOf(
            "void", "bool", "int8", "int16", "int32", "int64",
            "uint8", "uint16", "uint32", "uint64", "float", "double",
            "String", "UUID", "blob_id", "commit_id", "Any", "AnyConcept",
            "key", "optional", "vector", "set", "map", "tuple", "variant",
            "xarray", "vec", "mat"
        )
    }

    private fun highlightAttachment(element: DSMAttachmentDeclaration, holder: AnnotationHolder) {
        val identifier = element.identifier ?: return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(identifier.textRange)
            .textAttributes(ATTACHMENT_KEY)
            .create()
    }

}
