// This is a generated file. Not intended for manual editing.
package com.digitalsubstrate.dsm.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DSMLiteralValue extends PsiElement {

  @Nullable
  DSMBooleanLiteral getBooleanLiteral();

  @Nullable
  DSMEnumValueLiteralExpr getEnumValueLiteralExpr();

  @Nullable
  DSMListLiteral getListLiteral();

  @Nullable
  DSMNumberLiteral getNumberLiteral();

  @Nullable
  DSMStringLiteralExpr getStringLiteralExpr();

  @Nullable
  DSMUuidLiteral getUuidLiteral();

}
