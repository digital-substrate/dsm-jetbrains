// This is a generated file. Not intended for manual editing.
package com.digitalsubstrate.dsm.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.digitalsubstrate.dsm.psi.DSMTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class DSMParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, null);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return dsmFile(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // ATTACHMENT LANGLE typeReference COMMA typeReference RANGLE IDENTIFIER SEMICOLON
  public static boolean attachmentDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attachmentDeclaration")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATTACHMENT_DECLARATION, "<attachment declaration>");
    result_ = consumeTokens(builder_, 1, ATTACHMENT, LANGLE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, typeReference(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, COMMA)) && result_;
    result_ = pinned_ && report_error_(builder_, typeReference(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeTokens(builder_, -1, RANGLE, IDENTIFIER, SEMICOLON)) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DSMParser::attachmentRecover);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // MUTABLE? typeReference IDENTIFIER LPAREN parameterList? RPAREN SEMICOLON
  public static boolean attachmentFunctionDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attachmentFunctionDeclaration")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATTACHMENT_FUNCTION_DECLARATION, "<attachment function declaration>");
    result_ = attachmentFunctionDeclaration_0(builder_, level_ + 1);
    result_ = result_ && typeReference(builder_, level_ + 1);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, consumeTokens(builder_, -1, IDENTIFIER, LPAREN));
    result_ = pinned_ && report_error_(builder_, attachmentFunctionDeclaration_4(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeTokens(builder_, -1, RPAREN, SEMICOLON)) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DSMParser::attachmentFunctionRecover);
    return result_ || pinned_;
  }

  // MUTABLE?
  private static boolean attachmentFunctionDeclaration_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attachmentFunctionDeclaration_0")) return false;
    consumeToken(builder_, MUTABLE);
    return true;
  }

  // parameterList?
  private static boolean attachmentFunctionDeclaration_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attachmentFunctionDeclaration_4")) return false;
    parameterList(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // ATTACHMENT_FUNCTION_POOL IDENTIFIER UUID LBRACE attachmentFunctionDeclaration* RBRACE SEMICOLON
  public static boolean attachmentFunctionPoolDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attachmentFunctionPoolDeclaration")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATTACHMENT_FUNCTION_POOL_DECLARATION, "<attachment function pool declaration>");
    result_ = consumeTokens(builder_, 1, ATTACHMENT_FUNCTION_POOL, IDENTIFIER, UUID, LBRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, attachmentFunctionPoolDeclaration_4(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeTokens(builder_, -1, RBRACE, SEMICOLON)) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DSMParser::attachmentFunctionPoolRecover);
    return result_ || pinned_;
  }

  // attachmentFunctionDeclaration*
  private static boolean attachmentFunctionPoolDeclaration_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attachmentFunctionPoolDeclaration_4")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!attachmentFunctionDeclaration(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "attachmentFunctionPoolDeclaration_4", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !(NAMESPACE | FUNCTION_POOL | ATTACHMENT_FUNCTION_POOL | <<eof>>)
  static boolean attachmentFunctionPoolRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attachmentFunctionPoolRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !attachmentFunctionPoolRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // NAMESPACE | FUNCTION_POOL | ATTACHMENT_FUNCTION_POOL | <<eof>>
  private static boolean attachmentFunctionPoolRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attachmentFunctionPoolRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, NAMESPACE);
    if (!result_) result_ = consumeToken(builder_, FUNCTION_POOL);
    if (!result_) result_ = consumeToken(builder_, ATTACHMENT_FUNCTION_POOL);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // !(MUTABLE | VOID | BOOL | INT8 | INT16 | INT32 | INT64 | UINT8 | UINT16 | UINT32 | UINT64 | FLOAT | DOUBLE | STRING | UUID_TYPE | BLOB_ID | COMMIT_ID | ANY | ANY_CONCEPT | KEY | OPTIONAL | VECTOR | SET | MAP | TUPLE | VARIANT | XARRAY | VEC | MAT | IDENTIFIER | RBRACE)
  static boolean attachmentFunctionRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attachmentFunctionRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !attachmentFunctionRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // MUTABLE | VOID | BOOL | INT8 | INT16 | INT32 | INT64 | UINT8 | UINT16 | UINT32 | UINT64 | FLOAT | DOUBLE | STRING | UUID_TYPE | BLOB_ID | COMMIT_ID | ANY | ANY_CONCEPT | KEY | OPTIONAL | VECTOR | SET | MAP | TUPLE | VARIANT | XARRAY | VEC | MAT | IDENTIFIER | RBRACE
  private static boolean attachmentFunctionRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attachmentFunctionRecover_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, MUTABLE);
    if (!result_) result_ = consumeToken(builder_, VOID);
    if (!result_) result_ = consumeToken(builder_, BOOL);
    if (!result_) result_ = consumeToken(builder_, INT8);
    if (!result_) result_ = consumeToken(builder_, INT16);
    if (!result_) result_ = consumeToken(builder_, INT32);
    if (!result_) result_ = consumeToken(builder_, INT64);
    if (!result_) result_ = consumeToken(builder_, UINT8);
    if (!result_) result_ = consumeToken(builder_, UINT16);
    if (!result_) result_ = consumeToken(builder_, UINT32);
    if (!result_) result_ = consumeToken(builder_, UINT64);
    if (!result_) result_ = consumeToken(builder_, FLOAT);
    if (!result_) result_ = consumeToken(builder_, DOUBLE);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, UUID_TYPE);
    if (!result_) result_ = consumeToken(builder_, BLOB_ID);
    if (!result_) result_ = consumeToken(builder_, COMMIT_ID);
    if (!result_) result_ = consumeToken(builder_, ANY);
    if (!result_) result_ = consumeToken(builder_, ANY_CONCEPT);
    if (!result_) result_ = consumeToken(builder_, KEY);
    if (!result_) result_ = consumeToken(builder_, OPTIONAL);
    if (!result_) result_ = consumeToken(builder_, VECTOR);
    if (!result_) result_ = consumeToken(builder_, SET);
    if (!result_) result_ = consumeToken(builder_, MAP);
    if (!result_) result_ = consumeToken(builder_, TUPLE);
    if (!result_) result_ = consumeToken(builder_, VARIANT);
    if (!result_) result_ = consumeToken(builder_, XARRAY);
    if (!result_) result_ = consumeToken(builder_, VEC);
    if (!result_) result_ = consumeToken(builder_, MAT);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    return result_;
  }

  /* ********************************************************** */
  // !(ATTACHMENT | STRUCT | CONCEPT | ENUM | CLUB | MEMBERSHIP | RBRACE)
  static boolean attachmentRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attachmentRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !attachmentRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ATTACHMENT | STRUCT | CONCEPT | ENUM | CLUB | MEMBERSHIP | RBRACE
  private static boolean attachmentRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attachmentRecover_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, ATTACHMENT);
    if (!result_) result_ = consumeToken(builder_, STRUCT);
    if (!result_) result_ = consumeToken(builder_, CONCEPT);
    if (!result_) result_ = consumeToken(builder_, ENUM);
    if (!result_) result_ = consumeToken(builder_, CLUB);
    if (!result_) result_ = consumeToken(builder_, MEMBERSHIP);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    return result_;
  }

  /* ********************************************************** */
  // TRUE | FALSE
  public static boolean booleanLiteral(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "booleanLiteral")) return false;
    if (!nextTokenIs(builder_, "<boolean literal>", FALSE, TRUE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BOOLEAN_LITERAL, "<boolean literal>");
    result_ = consumeToken(builder_, TRUE);
    if (!result_) result_ = consumeToken(builder_, FALSE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // CLUB IDENTIFIER SEMICOLON
  public static boolean clubDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "clubDeclaration")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CLUB_DECLARATION, "<club declaration>");
    result_ = consumeTokens(builder_, 1, CLUB, IDENTIFIER, SEMICOLON);
    pinned_ = result_; // pin = 1
    exit_section_(builder_, level_, marker_, result_, pinned_, DSMParser::clubRecover);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // !(CLUB | MEMBERSHIP | STRUCT | CONCEPT | ENUM | ATTACHMENT | RBRACE)
  static boolean clubRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "clubRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !clubRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // CLUB | MEMBERSHIP | STRUCT | CONCEPT | ENUM | ATTACHMENT | RBRACE
  private static boolean clubRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "clubRecover_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, CLUB);
    if (!result_) result_ = consumeToken(builder_, MEMBERSHIP);
    if (!result_) result_ = consumeToken(builder_, STRUCT);
    if (!result_) result_ = consumeToken(builder_, CONCEPT);
    if (!result_) result_ = consumeToken(builder_, ENUM);
    if (!result_) result_ = consumeToken(builder_, ATTACHMENT);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    return result_;
  }

  /* ********************************************************** */
  // CONCEPT IDENTIFIER conceptInheritance? SEMICOLON
  public static boolean conceptDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conceptDeclaration")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONCEPT_DECLARATION, "<concept declaration>");
    result_ = consumeTokens(builder_, 1, CONCEPT, IDENTIFIER);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, conceptDeclaration_2(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DSMParser::conceptRecover);
    return result_ || pinned_;
  }

  // conceptInheritance?
  private static boolean conceptDeclaration_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conceptDeclaration_2")) return false;
    conceptInheritance(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // IS_A IDENTIFIER
  public static boolean conceptInheritance(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conceptInheritance")) return false;
    if (!nextTokenIs(builder_, IS_A)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IS_A, IDENTIFIER);
    exit_section_(builder_, marker_, CONCEPT_INHERITANCE, result_);
    return result_;
  }

  /* ********************************************************** */
  // !(CONCEPT | STRUCT | ENUM | CLUB | MEMBERSHIP | ATTACHMENT | RBRACE)
  static boolean conceptRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conceptRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !conceptRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // CONCEPT | STRUCT | ENUM | CLUB | MEMBERSHIP | ATTACHMENT | RBRACE
  private static boolean conceptRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conceptRecover_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, CONCEPT);
    if (!result_) result_ = consumeToken(builder_, STRUCT);
    if (!result_) result_ = consumeToken(builder_, ENUM);
    if (!result_) result_ = consumeToken(builder_, CLUB);
    if (!result_) result_ = consumeToken(builder_, MEMBERSHIP);
    if (!result_) result_ = consumeToken(builder_, ATTACHMENT);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    return result_;
  }

  /* ********************************************************** */
  // EQ literalValue
  public static boolean defaultValue(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defaultValue")) return false;
    if (!nextTokenIs(builder_, EQ)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, EQ);
    result_ = result_ && literalValue(builder_, level_ + 1);
    exit_section_(builder_, marker_, DEFAULT_VALUE, result_);
    return result_;
  }

  /* ********************************************************** */
  // topLevelDeclaration*
  static boolean dsmFile(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dsmFile")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!topLevelDeclaration(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "dsmFile", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ENUM IDENTIFIER LBRACE enumValueList RBRACE SEMICOLON
  public static boolean enumDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enumDeclaration")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ENUM_DECLARATION, "<enum declaration>");
    result_ = consumeTokens(builder_, 1, ENUM, IDENTIFIER, LBRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, enumValueList(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeTokens(builder_, -1, RBRACE, SEMICOLON)) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DSMParser::enumRecover);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // !(ENUM | STRUCT | CONCEPT | CLUB | MEMBERSHIP | ATTACHMENT | RBRACE)
  static boolean enumRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enumRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !enumRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ENUM | STRUCT | CONCEPT | CLUB | MEMBERSHIP | ATTACHMENT | RBRACE
  private static boolean enumRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enumRecover_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, ENUM);
    if (!result_) result_ = consumeToken(builder_, STRUCT);
    if (!result_) result_ = consumeToken(builder_, CONCEPT);
    if (!result_) result_ = consumeToken(builder_, CLUB);
    if (!result_) result_ = consumeToken(builder_, MEMBERSHIP);
    if (!result_) result_ = consumeToken(builder_, ATTACHMENT);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    return result_;
  }

  /* ********************************************************** */
  // identifierOrKeyword
  public static boolean enumValueDecl(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enumValueDecl")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ENUM_VALUE_DECL, "<enum value decl>");
    result_ = identifierOrKeyword(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // enumValueDecl (COMMA enumValueDecl)* COMMA?
  public static boolean enumValueList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enumValueList")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ENUM_VALUE_LIST, "<enum value list>");
    result_ = enumValueDecl(builder_, level_ + 1);
    result_ = result_ && enumValueList_1(builder_, level_ + 1);
    result_ = result_ && enumValueList_2(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA enumValueDecl)*
  private static boolean enumValueList_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enumValueList_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!enumValueList_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "enumValueList_1", pos_)) break;
    }
    return true;
  }

  // COMMA enumValueDecl
  private static boolean enumValueList_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enumValueList_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && enumValueDecl(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // COMMA?
  private static boolean enumValueList_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enumValueList_2")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // ENUM_VALUE
  public static boolean enumValueLiteralExpr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enumValueLiteralExpr")) return false;
    if (!nextTokenIs(builder_, ENUM_VALUE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ENUM_VALUE);
    exit_section_(builder_, marker_, ENUM_VALUE_LITERAL_EXPR, result_);
    return result_;
  }

  /* ********************************************************** */
  // typeReference identifierOrKeyword defaultValue? SEMICOLON
  public static boolean fieldDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldDeclaration")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FIELD_DECLARATION, "<field declaration>");
    result_ = typeReference(builder_, level_ + 1);
    result_ = result_ && identifierOrKeyword(builder_, level_ + 1);
    result_ = result_ && fieldDeclaration_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    exit_section_(builder_, level_, marker_, result_, false, DSMParser::fieldRecover);
    return result_;
  }

  // defaultValue?
  private static boolean fieldDeclaration_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldDeclaration_2")) return false;
    defaultValue(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // !(VOID | BOOL | INT8 | INT16 | INT32 | INT64 | UINT8 | UINT16 | UINT32 | UINT64 | FLOAT | DOUBLE | STRING | UUID_TYPE | BLOB_ID | COMMIT_ID | ANY | ANY_CONCEPT | KEY | OPTIONAL | VECTOR | SET | MAP | TUPLE | VARIANT | XARRAY | VEC | MAT | IDENTIFIER | RBRACE)
  static boolean fieldRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !fieldRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // VOID | BOOL | INT8 | INT16 | INT32 | INT64 | UINT8 | UINT16 | UINT32 | UINT64 | FLOAT | DOUBLE | STRING | UUID_TYPE | BLOB_ID | COMMIT_ID | ANY | ANY_CONCEPT | KEY | OPTIONAL | VECTOR | SET | MAP | TUPLE | VARIANT | XARRAY | VEC | MAT | IDENTIFIER | RBRACE
  private static boolean fieldRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldRecover_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, VOID);
    if (!result_) result_ = consumeToken(builder_, BOOL);
    if (!result_) result_ = consumeToken(builder_, INT8);
    if (!result_) result_ = consumeToken(builder_, INT16);
    if (!result_) result_ = consumeToken(builder_, INT32);
    if (!result_) result_ = consumeToken(builder_, INT64);
    if (!result_) result_ = consumeToken(builder_, UINT8);
    if (!result_) result_ = consumeToken(builder_, UINT16);
    if (!result_) result_ = consumeToken(builder_, UINT32);
    if (!result_) result_ = consumeToken(builder_, UINT64);
    if (!result_) result_ = consumeToken(builder_, FLOAT);
    if (!result_) result_ = consumeToken(builder_, DOUBLE);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, UUID_TYPE);
    if (!result_) result_ = consumeToken(builder_, BLOB_ID);
    if (!result_) result_ = consumeToken(builder_, COMMIT_ID);
    if (!result_) result_ = consumeToken(builder_, ANY);
    if (!result_) result_ = consumeToken(builder_, ANY_CONCEPT);
    if (!result_) result_ = consumeToken(builder_, KEY);
    if (!result_) result_ = consumeToken(builder_, OPTIONAL);
    if (!result_) result_ = consumeToken(builder_, VECTOR);
    if (!result_) result_ = consumeToken(builder_, SET);
    if (!result_) result_ = consumeToken(builder_, MAP);
    if (!result_) result_ = consumeToken(builder_, TUPLE);
    if (!result_) result_ = consumeToken(builder_, VARIANT);
    if (!result_) result_ = consumeToken(builder_, XARRAY);
    if (!result_) result_ = consumeToken(builder_, VEC);
    if (!result_) result_ = consumeToken(builder_, MAT);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    return result_;
  }

  /* ********************************************************** */
  // typeReference IDENTIFIER LPAREN parameterList? RPAREN SEMICOLON
  public static boolean functionDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclaration")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_DECLARATION, "<function declaration>");
    result_ = typeReference(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, consumeTokens(builder_, -1, IDENTIFIER, LPAREN));
    result_ = pinned_ && report_error_(builder_, functionDeclaration_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeTokens(builder_, -1, RPAREN, SEMICOLON)) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DSMParser::functionRecover);
    return result_ || pinned_;
  }

  // parameterList?
  private static boolean functionDeclaration_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclaration_3")) return false;
    parameterList(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // FUNCTION_POOL IDENTIFIER UUID LBRACE functionDeclaration* RBRACE SEMICOLON
  public static boolean functionPoolDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionPoolDeclaration")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_POOL_DECLARATION, "<function pool declaration>");
    result_ = consumeTokens(builder_, 1, FUNCTION_POOL, IDENTIFIER, UUID, LBRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, functionPoolDeclaration_4(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeTokens(builder_, -1, RBRACE, SEMICOLON)) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DSMParser::functionPoolRecover);
    return result_ || pinned_;
  }

  // functionDeclaration*
  private static boolean functionPoolDeclaration_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionPoolDeclaration_4")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!functionDeclaration(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "functionPoolDeclaration_4", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !(NAMESPACE | FUNCTION_POOL | ATTACHMENT_FUNCTION_POOL | <<eof>>)
  static boolean functionPoolRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionPoolRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !functionPoolRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // NAMESPACE | FUNCTION_POOL | ATTACHMENT_FUNCTION_POOL | <<eof>>
  private static boolean functionPoolRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionPoolRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, NAMESPACE);
    if (!result_) result_ = consumeToken(builder_, FUNCTION_POOL);
    if (!result_) result_ = consumeToken(builder_, ATTACHMENT_FUNCTION_POOL);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // !(VOID | BOOL | INT8 | INT16 | INT32 | INT64 | UINT8 | UINT16 | UINT32 | UINT64 | FLOAT | DOUBLE | STRING | UUID_TYPE | BLOB_ID | COMMIT_ID | ANY | ANY_CONCEPT | KEY | OPTIONAL | VECTOR | SET | MAP | TUPLE | VARIANT | XARRAY | VEC | MAT | IDENTIFIER | RBRACE)
  static boolean functionRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !functionRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // VOID | BOOL | INT8 | INT16 | INT32 | INT64 | UINT8 | UINT16 | UINT32 | UINT64 | FLOAT | DOUBLE | STRING | UUID_TYPE | BLOB_ID | COMMIT_ID | ANY | ANY_CONCEPT | KEY | OPTIONAL | VECTOR | SET | MAP | TUPLE | VARIANT | XARRAY | VEC | MAT | IDENTIFIER | RBRACE
  private static boolean functionRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionRecover_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, VOID);
    if (!result_) result_ = consumeToken(builder_, BOOL);
    if (!result_) result_ = consumeToken(builder_, INT8);
    if (!result_) result_ = consumeToken(builder_, INT16);
    if (!result_) result_ = consumeToken(builder_, INT32);
    if (!result_) result_ = consumeToken(builder_, INT64);
    if (!result_) result_ = consumeToken(builder_, UINT8);
    if (!result_) result_ = consumeToken(builder_, UINT16);
    if (!result_) result_ = consumeToken(builder_, UINT32);
    if (!result_) result_ = consumeToken(builder_, UINT64);
    if (!result_) result_ = consumeToken(builder_, FLOAT);
    if (!result_) result_ = consumeToken(builder_, DOUBLE);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, UUID_TYPE);
    if (!result_) result_ = consumeToken(builder_, BLOB_ID);
    if (!result_) result_ = consumeToken(builder_, COMMIT_ID);
    if (!result_) result_ = consumeToken(builder_, ANY);
    if (!result_) result_ = consumeToken(builder_, ANY_CONCEPT);
    if (!result_) result_ = consumeToken(builder_, KEY);
    if (!result_) result_ = consumeToken(builder_, OPTIONAL);
    if (!result_) result_ = consumeToken(builder_, VECTOR);
    if (!result_) result_ = consumeToken(builder_, SET);
    if (!result_) result_ = consumeToken(builder_, MAP);
    if (!result_) result_ = consumeToken(builder_, TUPLE);
    if (!result_) result_ = consumeToken(builder_, VARIANT);
    if (!result_) result_ = consumeToken(builder_, XARRAY);
    if (!result_) result_ = consumeToken(builder_, VEC);
    if (!result_) result_ = consumeToken(builder_, MAT);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    return result_;
  }

  /* ********************************************************** */
  // KEY LANGLE typeReference RANGLE
  //     | OPTIONAL LANGLE typeReference RANGLE
  //     | VECTOR LANGLE typeReference RANGLE
  //     | SET LANGLE typeReference RANGLE
  //     | MAP LANGLE typeReference COMMA typeReference RANGLE
  //     | TUPLE LANGLE typeReferenceList RANGLE
  //     | VARIANT LANGLE typeReferenceList RANGLE
  //     | XARRAY LANGLE typeReference RANGLE
  //     | VEC LANGLE typeReference COMMA NUMBER RANGLE
  //     | MAT LANGLE typeReference COMMA NUMBER COMMA NUMBER RANGLE
  public static boolean genericType(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "genericType")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, GENERIC_TYPE, "<generic type>");
    result_ = genericType_0(builder_, level_ + 1);
    if (!result_) result_ = genericType_1(builder_, level_ + 1);
    if (!result_) result_ = genericType_2(builder_, level_ + 1);
    if (!result_) result_ = genericType_3(builder_, level_ + 1);
    if (!result_) result_ = genericType_4(builder_, level_ + 1);
    if (!result_) result_ = genericType_5(builder_, level_ + 1);
    if (!result_) result_ = genericType_6(builder_, level_ + 1);
    if (!result_) result_ = genericType_7(builder_, level_ + 1);
    if (!result_) result_ = genericType_8(builder_, level_ + 1);
    if (!result_) result_ = genericType_9(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // KEY LANGLE typeReference RANGLE
  private static boolean genericType_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "genericType_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, KEY, LANGLE);
    result_ = result_ && typeReference(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RANGLE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // OPTIONAL LANGLE typeReference RANGLE
  private static boolean genericType_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "genericType_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, OPTIONAL, LANGLE);
    result_ = result_ && typeReference(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RANGLE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // VECTOR LANGLE typeReference RANGLE
  private static boolean genericType_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "genericType_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, VECTOR, LANGLE);
    result_ = result_ && typeReference(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RANGLE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // SET LANGLE typeReference RANGLE
  private static boolean genericType_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "genericType_3")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, SET, LANGLE);
    result_ = result_ && typeReference(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RANGLE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // MAP LANGLE typeReference COMMA typeReference RANGLE
  private static boolean genericType_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "genericType_4")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, MAP, LANGLE);
    result_ = result_ && typeReference(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COMMA);
    result_ = result_ && typeReference(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RANGLE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // TUPLE LANGLE typeReferenceList RANGLE
  private static boolean genericType_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "genericType_5")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, TUPLE, LANGLE);
    result_ = result_ && typeReferenceList(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RANGLE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // VARIANT LANGLE typeReferenceList RANGLE
  private static boolean genericType_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "genericType_6")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, VARIANT, LANGLE);
    result_ = result_ && typeReferenceList(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RANGLE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // XARRAY LANGLE typeReference RANGLE
  private static boolean genericType_7(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "genericType_7")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, XARRAY, LANGLE);
    result_ = result_ && typeReference(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RANGLE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // VEC LANGLE typeReference COMMA NUMBER RANGLE
  private static boolean genericType_8(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "genericType_8")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, VEC, LANGLE);
    result_ = result_ && typeReference(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, COMMA, NUMBER, RANGLE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // MAT LANGLE typeReference COMMA NUMBER COMMA NUMBER RANGLE
  private static boolean genericType_9(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "genericType_9")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, MAT, LANGLE);
    result_ = result_ && typeReference(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, COMMA, NUMBER, COMMA, NUMBER, RANGLE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER
  //     | VOID | BOOL
  //     | INT8 | INT16 | INT32 | INT64
  //     | UINT8 | UINT16 | UINT32 | UINT64
  //     | FLOAT | DOUBLE
  //     | STRING | UUID_TYPE | BLOB_ID | COMMIT_ID
  //     | ANY | ANY_CONCEPT
  //     | KEY | OPTIONAL | VECTOR | SET | MAP | TUPLE | VARIANT | XARRAY | VEC | MAT
  public static boolean identifierOrKeyword(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "identifierOrKeyword")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IDENTIFIER_OR_KEYWORD, "<identifier or keyword>");
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, VOID);
    if (!result_) result_ = consumeToken(builder_, BOOL);
    if (!result_) result_ = consumeToken(builder_, INT8);
    if (!result_) result_ = consumeToken(builder_, INT16);
    if (!result_) result_ = consumeToken(builder_, INT32);
    if (!result_) result_ = consumeToken(builder_, INT64);
    if (!result_) result_ = consumeToken(builder_, UINT8);
    if (!result_) result_ = consumeToken(builder_, UINT16);
    if (!result_) result_ = consumeToken(builder_, UINT32);
    if (!result_) result_ = consumeToken(builder_, UINT64);
    if (!result_) result_ = consumeToken(builder_, FLOAT);
    if (!result_) result_ = consumeToken(builder_, DOUBLE);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, UUID_TYPE);
    if (!result_) result_ = consumeToken(builder_, BLOB_ID);
    if (!result_) result_ = consumeToken(builder_, COMMIT_ID);
    if (!result_) result_ = consumeToken(builder_, ANY);
    if (!result_) result_ = consumeToken(builder_, ANY_CONCEPT);
    if (!result_) result_ = consumeToken(builder_, KEY);
    if (!result_) result_ = consumeToken(builder_, OPTIONAL);
    if (!result_) result_ = consumeToken(builder_, VECTOR);
    if (!result_) result_ = consumeToken(builder_, SET);
    if (!result_) result_ = consumeToken(builder_, MAP);
    if (!result_) result_ = consumeToken(builder_, TUPLE);
    if (!result_) result_ = consumeToken(builder_, VARIANT);
    if (!result_) result_ = consumeToken(builder_, XARRAY);
    if (!result_) result_ = consumeToken(builder_, VEC);
    if (!result_) result_ = consumeToken(builder_, MAT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // LBRACE literalValue (COMMA literalValue)* COMMA? RBRACE
  public static boolean listLiteral(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "listLiteral")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && literalValue(builder_, level_ + 1);
    result_ = result_ && listLiteral_2(builder_, level_ + 1);
    result_ = result_ && listLiteral_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, LIST_LITERAL, result_);
    return result_;
  }

  // (COMMA literalValue)*
  private static boolean listLiteral_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "listLiteral_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!listLiteral_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "listLiteral_2", pos_)) break;
    }
    return true;
  }

  // COMMA literalValue
  private static boolean listLiteral_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "listLiteral_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && literalValue(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // COMMA?
  private static boolean listLiteral_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "listLiteral_3")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // booleanLiteral
  //     | numberLiteral
  //     | stringLiteralExpr
  //     | uuidLiteral
  //     | enumValueLiteralExpr
  //     | listLiteral
  public static boolean literalValue(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "literalValue")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LITERAL_VALUE, "<literal value>");
    result_ = booleanLiteral(builder_, level_ + 1);
    if (!result_) result_ = numberLiteral(builder_, level_ + 1);
    if (!result_) result_ = stringLiteralExpr(builder_, level_ + 1);
    if (!result_) result_ = uuidLiteral(builder_, level_ + 1);
    if (!result_) result_ = enumValueLiteralExpr(builder_, level_ + 1);
    if (!result_) result_ = listLiteral(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // MEMBERSHIP IDENTIFIER IDENTIFIER SEMICOLON
  public static boolean membershipDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "membershipDeclaration")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MEMBERSHIP_DECLARATION, "<membership declaration>");
    result_ = consumeTokens(builder_, 1, MEMBERSHIP, IDENTIFIER, IDENTIFIER, SEMICOLON);
    pinned_ = result_; // pin = 1
    exit_section_(builder_, level_, marker_, result_, pinned_, DSMParser::membershipRecover);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // !(MEMBERSHIP | STRUCT | CONCEPT | ENUM | CLUB | ATTACHMENT | RBRACE)
  static boolean membershipRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "membershipRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !membershipRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // MEMBERSHIP | STRUCT | CONCEPT | ENUM | CLUB | ATTACHMENT | RBRACE
  private static boolean membershipRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "membershipRecover_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, MEMBERSHIP);
    if (!result_) result_ = consumeToken(builder_, STRUCT);
    if (!result_) result_ = consumeToken(builder_, CONCEPT);
    if (!result_) result_ = consumeToken(builder_, ENUM);
    if (!result_) result_ = consumeToken(builder_, CLUB);
    if (!result_) result_ = consumeToken(builder_, ATTACHMENT);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    return result_;
  }

  /* ********************************************************** */
  // namespaceElement*
  public static boolean namespaceBody(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namespaceBody")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NAMESPACE_BODY, "<namespace body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!namespaceElement(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "namespaceBody", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // NAMESPACE IDENTIFIER UUID LBRACE namespaceBody RBRACE SEMICOLON
  public static boolean namespaceDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namespaceDeclaration")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NAMESPACE_DECLARATION, "<namespace declaration>");
    result_ = consumeTokens(builder_, 1, NAMESPACE, IDENTIFIER, UUID, LBRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, namespaceBody(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeTokens(builder_, -1, RBRACE, SEMICOLON)) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DSMParser::namespaceRecover);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // conceptDeclaration
  //     | structDeclaration
  //     | enumDeclaration
  //     | clubDeclaration
  //     | membershipDeclaration
  //     | attachmentDeclaration
  static boolean namespaceElement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namespaceElement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = conceptDeclaration(builder_, level_ + 1);
    if (!result_) result_ = structDeclaration(builder_, level_ + 1);
    if (!result_) result_ = enumDeclaration(builder_, level_ + 1);
    if (!result_) result_ = clubDeclaration(builder_, level_ + 1);
    if (!result_) result_ = membershipDeclaration(builder_, level_ + 1);
    if (!result_) result_ = attachmentDeclaration(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // !(NAMESPACE | FUNCTION_POOL | ATTACHMENT_FUNCTION_POOL | <<eof>>)
  static boolean namespaceRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namespaceRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !namespaceRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // NAMESPACE | FUNCTION_POOL | ATTACHMENT_FUNCTION_POOL | <<eof>>
  private static boolean namespaceRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namespaceRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, NAMESPACE);
    if (!result_) result_ = consumeToken(builder_, FUNCTION_POOL);
    if (!result_) result_ = consumeToken(builder_, ATTACHMENT_FUNCTION_POOL);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // NUMBER
  public static boolean numberLiteral(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "numberLiteral")) return false;
    if (!nextTokenIs(builder_, NUMBER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, NUMBER);
    exit_section_(builder_, marker_, NUMBER_LITERAL, result_);
    return result_;
  }

  /* ********************************************************** */
  // typeReference identifierOrKeyword
  public static boolean parameter(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameter")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PARAMETER, "<parameter>");
    result_ = typeReference(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && identifierOrKeyword(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // parameter (COMMA parameter)*
  public static boolean parameterList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameterList")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PARAMETER_LIST, "<parameter list>");
    result_ = parameter(builder_, level_ + 1);
    result_ = result_ && parameterList_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA parameter)*
  private static boolean parameterList_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameterList_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!parameterList_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "parameterList_1", pos_)) break;
    }
    return true;
  }

  // COMMA parameter
  private static boolean parameterList_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameterList_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && parameter(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // VOID | BOOL
  //     | INT8 | INT16 | INT32 | INT64
  //     | UINT8 | UINT16 | UINT32 | UINT64
  //     | FLOAT | DOUBLE
  //     | STRING | UUID_TYPE | BLOB_ID | COMMIT_ID
  //     | ANY | ANY_CONCEPT
  public static boolean primitiveType(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "primitiveType")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PRIMITIVE_TYPE, "<primitive type>");
    result_ = consumeToken(builder_, VOID);
    if (!result_) result_ = consumeToken(builder_, BOOL);
    if (!result_) result_ = consumeToken(builder_, INT8);
    if (!result_) result_ = consumeToken(builder_, INT16);
    if (!result_) result_ = consumeToken(builder_, INT32);
    if (!result_) result_ = consumeToken(builder_, INT64);
    if (!result_) result_ = consumeToken(builder_, UINT8);
    if (!result_) result_ = consumeToken(builder_, UINT16);
    if (!result_) result_ = consumeToken(builder_, UINT32);
    if (!result_) result_ = consumeToken(builder_, UINT64);
    if (!result_) result_ = consumeToken(builder_, FLOAT);
    if (!result_) result_ = consumeToken(builder_, DOUBLE);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, UUID_TYPE);
    if (!result_) result_ = consumeToken(builder_, BLOB_ID);
    if (!result_) result_ = consumeToken(builder_, COMMIT_ID);
    if (!result_) result_ = consumeToken(builder_, ANY);
    if (!result_) result_ = consumeToken(builder_, ANY_CONCEPT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // STRING_LITERAL
  public static boolean stringLiteralExpr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stringLiteralExpr")) return false;
    if (!nextTokenIs(builder_, STRING_LITERAL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, STRING_LITERAL);
    exit_section_(builder_, marker_, STRING_LITERAL_EXPR, result_);
    return result_;
  }

  /* ********************************************************** */
  // STRUCT IDENTIFIER LBRACE fieldDeclaration* RBRACE SEMICOLON
  public static boolean structDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "structDeclaration")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, STRUCT_DECLARATION, "<struct declaration>");
    result_ = consumeTokens(builder_, 1, STRUCT, IDENTIFIER, LBRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, structDeclaration_3(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeTokens(builder_, -1, RBRACE, SEMICOLON)) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DSMParser::structRecover);
    return result_ || pinned_;
  }

  // fieldDeclaration*
  private static boolean structDeclaration_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "structDeclaration_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!fieldDeclaration(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "structDeclaration_3", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !(STRUCT | CONCEPT | ENUM | CLUB | MEMBERSHIP | ATTACHMENT | RBRACE)
  static boolean structRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "structRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !structRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // STRUCT | CONCEPT | ENUM | CLUB | MEMBERSHIP | ATTACHMENT | RBRACE
  private static boolean structRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "structRecover_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRUCT);
    if (!result_) result_ = consumeToken(builder_, CONCEPT);
    if (!result_) result_ = consumeToken(builder_, ENUM);
    if (!result_) result_ = consumeToken(builder_, CLUB);
    if (!result_) result_ = consumeToken(builder_, MEMBERSHIP);
    if (!result_) result_ = consumeToken(builder_, ATTACHMENT);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    return result_;
  }

  /* ********************************************************** */
  // namespaceDeclaration
  //     | functionPoolDeclaration
  //     | attachmentFunctionPoolDeclaration
  static boolean topLevelDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "topLevelDeclaration")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = namespaceDeclaration(builder_, level_ + 1);
    if (!result_) result_ = functionPoolDeclaration(builder_, level_ + 1);
    if (!result_) result_ = attachmentFunctionPoolDeclaration(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // primitiveType
  //     | genericType
  //     | userTypeReference
  public static boolean typeReference(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeReference")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TYPE_REFERENCE, "<type reference>");
    result_ = primitiveType(builder_, level_ + 1);
    if (!result_) result_ = genericType(builder_, level_ + 1);
    if (!result_) result_ = userTypeReference(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // typeReference (COMMA typeReference)*
  public static boolean typeReferenceList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeReferenceList")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TYPE_REFERENCE_LIST, "<type reference list>");
    result_ = typeReference(builder_, level_ + 1);
    result_ = result_ && typeReferenceList_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA typeReference)*
  private static boolean typeReferenceList_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeReferenceList_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!typeReferenceList_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "typeReferenceList_1", pos_)) break;
    }
    return true;
  }

  // COMMA typeReference
  private static boolean typeReferenceList_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeReferenceList_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && typeReference(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean userTypeReference(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "userTypeReference")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    exit_section_(builder_, marker_, USER_TYPE_REFERENCE, result_);
    return result_;
  }

  /* ********************************************************** */
  // UUID
  public static boolean uuidLiteral(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "uuidLiteral")) return false;
    if (!nextTokenIs(builder_, UUID)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, UUID);
    exit_section_(builder_, marker_, UUID_LITERAL, result_);
    return result_;
  }

}
