// This is a generated file. Not intended for manual editing.
package com.digitalsubstrate.dsm.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.digitalsubstrate.dsm.stub.DSMStubElementTypesKt;
import com.digitalsubstrate.dsm.psi.impl.*;

public interface DSMTypes {

  IElementType ATTACHMENT_DECLARATION = new DSMElementType("ATTACHMENT_DECLARATION");
  IElementType ATTACHMENT_FUNCTION_DECLARATION = new DSMElementType("ATTACHMENT_FUNCTION_DECLARATION");
  IElementType ATTACHMENT_FUNCTION_POOL_DECLARATION = new DSMElementType("ATTACHMENT_FUNCTION_POOL_DECLARATION");
  IElementType BOOLEAN_LITERAL = new DSMElementType("BOOLEAN_LITERAL");
  IElementType CLUB_DECLARATION = DSMStubElementTypesKt.factory("CLUB_DECLARATION");
  IElementType CONCEPT_DECLARATION = DSMStubElementTypesKt.factory("CONCEPT_DECLARATION");
  IElementType CONCEPT_INHERITANCE = new DSMElementType("CONCEPT_INHERITANCE");
  IElementType DEFAULT_VALUE = new DSMElementType("DEFAULT_VALUE");
  IElementType ENUM_DECLARATION = DSMStubElementTypesKt.factory("ENUM_DECLARATION");
  IElementType ENUM_VALUE_DECL = new DSMElementType("ENUM_VALUE_DECL");
  IElementType ENUM_VALUE_LIST = new DSMElementType("ENUM_VALUE_LIST");
  IElementType ENUM_VALUE_LITERAL_EXPR = new DSMElementType("ENUM_VALUE_LITERAL_EXPR");
  IElementType FIELD_DECLARATION = new DSMElementType("FIELD_DECLARATION");
  IElementType FUNCTION_DECLARATION = new DSMElementType("FUNCTION_DECLARATION");
  IElementType FUNCTION_POOL_DECLARATION = new DSMElementType("FUNCTION_POOL_DECLARATION");
  IElementType GENERIC_TYPE = new DSMElementType("GENERIC_TYPE");
  IElementType IDENTIFIER_OR_KEYWORD = new DSMElementType("IDENTIFIER_OR_KEYWORD");
  IElementType LIST_LITERAL = new DSMElementType("LIST_LITERAL");
  IElementType LITERAL_VALUE = new DSMElementType("LITERAL_VALUE");
  IElementType MEMBERSHIP_DECLARATION = new DSMElementType("MEMBERSHIP_DECLARATION");
  IElementType NAMESPACE_BODY = new DSMElementType("NAMESPACE_BODY");
  IElementType NAMESPACE_DECLARATION = new DSMElementType("NAMESPACE_DECLARATION");
  IElementType NUMBER_LITERAL = new DSMElementType("NUMBER_LITERAL");
  IElementType PARAMETER = new DSMElementType("PARAMETER");
  IElementType PARAMETER_LIST = new DSMElementType("PARAMETER_LIST");
  IElementType PRIMITIVE_TYPE = new DSMElementType("PRIMITIVE_TYPE");
  IElementType STRING_LITERAL_EXPR = new DSMElementType("STRING_LITERAL_EXPR");
  IElementType STRUCT_DECLARATION = DSMStubElementTypesKt.factory("STRUCT_DECLARATION");
  IElementType TYPE_REFERENCE = new DSMElementType("TYPE_REFERENCE");
  IElementType TYPE_REFERENCE_LIST = new DSMElementType("TYPE_REFERENCE_LIST");
  IElementType USER_TYPE_REFERENCE = new DSMElementType("USER_TYPE_REFERENCE");
  IElementType UUID_LITERAL = new DSMElementType("UUID_LITERAL");

  IElementType ANY = new DSMTokenType("any");
  IElementType ANY_CONCEPT = new DSMTokenType("any_concept");
  IElementType ATTACHMENT = new DSMTokenType("attachment");
  IElementType ATTACHMENT_FUNCTION_POOL = new DSMTokenType("attachment_function_pool");
  IElementType BLOB_ID = new DSMTokenType("blob_id");
  IElementType BOOL = new DSMTokenType("bool");
  IElementType CLUB = new DSMTokenType("club");
  IElementType COLON = new DSMTokenType(":");
  IElementType COMMA = new DSMTokenType(",");
  IElementType COMMIT_ID = new DSMTokenType("commit_id");
  IElementType CONCEPT = new DSMTokenType("concept");
  IElementType DOC_STRING = new DSMTokenType("DOC_STRING");
  IElementType DOT = new DSMTokenType(".");
  IElementType DOUBLE = new DSMTokenType("double");
  IElementType ENUM = new DSMTokenType("enum");
  IElementType ENUM_VALUE = new DSMTokenType("ENUM_VALUE");
  IElementType EQ = new DSMTokenType("=");
  IElementType FALSE = new DSMTokenType("false");
  IElementType FLOAT = new DSMTokenType("float");
  IElementType FUNCTION_POOL = new DSMTokenType("function_pool");
  IElementType IDENTIFIER = new DSMTokenType("IDENTIFIER");
  IElementType INT16 = new DSMTokenType("int16");
  IElementType INT32 = new DSMTokenType("int32");
  IElementType INT64 = new DSMTokenType("int64");
  IElementType INT8 = new DSMTokenType("int8");
  IElementType IS_A = new DSMTokenType("is a");
  IElementType KEY = new DSMTokenType("key");
  IElementType LANGLE = new DSMTokenType("<");
  IElementType LBRACE = new DSMTokenType("{");
  IElementType LBRACKET = new DSMTokenType("[");
  IElementType LINE_COMMENT = new DSMTokenType("LINE_COMMENT");
  IElementType LPAREN = new DSMTokenType("(");
  IElementType MAP = new DSMTokenType("map");
  IElementType MAT = new DSMTokenType("mat");
  IElementType MEMBERSHIP = new DSMTokenType("membership");
  IElementType MUTABLE = new DSMTokenType("mutable");
  IElementType NAMESPACE = new DSMTokenType("namespace");
  IElementType NUMBER = new DSMTokenType("NUMBER");
  IElementType OPTIONAL = new DSMTokenType("optional");
  IElementType RANGLE = new DSMTokenType(">");
  IElementType RBRACE = new DSMTokenType("}");
  IElementType RBRACKET = new DSMTokenType("]");
  IElementType RPAREN = new DSMTokenType(")");
  IElementType SEMICOLON = new DSMTokenType(";");
  IElementType SET = new DSMTokenType("set");
  IElementType STRING = new DSMTokenType("string");
  IElementType STRING_LITERAL = new DSMTokenType("STRING_LITERAL");
  IElementType STRUCT = new DSMTokenType("struct");
  IElementType TRUE = new DSMTokenType("true");
  IElementType TUPLE = new DSMTokenType("tuple");
  IElementType UINT16 = new DSMTokenType("uint16");
  IElementType UINT32 = new DSMTokenType("uint32");
  IElementType UINT64 = new DSMTokenType("uint64");
  IElementType UINT8 = new DSMTokenType("uint8");
  IElementType UUID = new DSMTokenType("UUID");
  IElementType UUID_TYPE = new DSMTokenType("uuid");
  IElementType VARIANT = new DSMTokenType("variant");
  IElementType VEC = new DSMTokenType("vec");
  IElementType VECTOR = new DSMTokenType("vector");
  IElementType VOID = new DSMTokenType("void");
  IElementType XARRAY = new DSMTokenType("xarray");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ATTACHMENT_DECLARATION) {
        return new DSMAttachmentDeclarationImpl(node);
      }
      else if (type == ATTACHMENT_FUNCTION_DECLARATION) {
        return new DSMAttachmentFunctionDeclarationImpl(node);
      }
      else if (type == ATTACHMENT_FUNCTION_POOL_DECLARATION) {
        return new DSMAttachmentFunctionPoolDeclarationImpl(node);
      }
      else if (type == BOOLEAN_LITERAL) {
        return new DSMBooleanLiteralImpl(node);
      }
      else if (type == CLUB_DECLARATION) {
        return new DSMClubDeclarationImpl(node);
      }
      else if (type == CONCEPT_DECLARATION) {
        return new DSMConceptDeclarationImpl(node);
      }
      else if (type == CONCEPT_INHERITANCE) {
        return new DSMConceptInheritanceImpl(node);
      }
      else if (type == DEFAULT_VALUE) {
        return new DSMDefaultValueImpl(node);
      }
      else if (type == ENUM_DECLARATION) {
        return new DSMEnumDeclarationImpl(node);
      }
      else if (type == ENUM_VALUE_DECL) {
        return new DSMEnumValueDeclImpl(node);
      }
      else if (type == ENUM_VALUE_LIST) {
        return new DSMEnumValueListImpl(node);
      }
      else if (type == ENUM_VALUE_LITERAL_EXPR) {
        return new DSMEnumValueLiteralExprImpl(node);
      }
      else if (type == FIELD_DECLARATION) {
        return new DSMFieldDeclarationImpl(node);
      }
      else if (type == FUNCTION_DECLARATION) {
        return new DSMFunctionDeclarationImpl(node);
      }
      else if (type == FUNCTION_POOL_DECLARATION) {
        return new DSMFunctionPoolDeclarationImpl(node);
      }
      else if (type == GENERIC_TYPE) {
        return new DSMGenericTypeImpl(node);
      }
      else if (type == IDENTIFIER_OR_KEYWORD) {
        return new DSMIdentifierOrKeywordImpl(node);
      }
      else if (type == LIST_LITERAL) {
        return new DSMListLiteralImpl(node);
      }
      else if (type == LITERAL_VALUE) {
        return new DSMLiteralValueImpl(node);
      }
      else if (type == MEMBERSHIP_DECLARATION) {
        return new DSMMembershipDeclarationImpl(node);
      }
      else if (type == NAMESPACE_BODY) {
        return new DSMNamespaceBodyImpl(node);
      }
      else if (type == NAMESPACE_DECLARATION) {
        return new DSMNamespaceDeclarationImpl(node);
      }
      else if (type == NUMBER_LITERAL) {
        return new DSMNumberLiteralImpl(node);
      }
      else if (type == PARAMETER) {
        return new DSMParameterImpl(node);
      }
      else if (type == PARAMETER_LIST) {
        return new DSMParameterListImpl(node);
      }
      else if (type == PRIMITIVE_TYPE) {
        return new DSMPrimitiveTypeImpl(node);
      }
      else if (type == STRING_LITERAL_EXPR) {
        return new DSMStringLiteralExprImpl(node);
      }
      else if (type == STRUCT_DECLARATION) {
        return new DSMStructDeclarationImpl(node);
      }
      else if (type == TYPE_REFERENCE) {
        return new DSMTypeReferenceImpl(node);
      }
      else if (type == TYPE_REFERENCE_LIST) {
        return new DSMTypeReferenceListImpl(node);
      }
      else if (type == USER_TYPE_REFERENCE) {
        return new DSMUserTypeReferenceImpl(node);
      }
      else if (type == UUID_LITERAL) {
        return new DSMUuidLiteralImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
