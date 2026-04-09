// This is a generated file. Not intended for manual editing.
package com.digitalsubstrate.dsm.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.digitalsubstrate.dsm.psi.DSMTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.digitalsubstrate.dsm.psi.*;

public class DSMLiteralValueImpl extends ASTWrapperPsiElement implements DSMLiteralValue {

  public DSMLiteralValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DSMVisitor visitor) {
    visitor.visitLiteralValue(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DSMVisitor) accept((DSMVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DSMBooleanLiteral getBooleanLiteral() {
    return PsiTreeUtil.getChildOfType(this, DSMBooleanLiteral.class);
  }

  @Override
  @Nullable
  public DSMEnumValueLiteralExpr getEnumValueLiteralExpr() {
    return PsiTreeUtil.getChildOfType(this, DSMEnumValueLiteralExpr.class);
  }

  @Override
  @Nullable
  public DSMListLiteral getListLiteral() {
    return PsiTreeUtil.getChildOfType(this, DSMListLiteral.class);
  }

  @Override
  @Nullable
  public DSMNumberLiteral getNumberLiteral() {
    return PsiTreeUtil.getChildOfType(this, DSMNumberLiteral.class);
  }

  @Override
  @Nullable
  public DSMStringLiteralExpr getStringLiteralExpr() {
    return PsiTreeUtil.getChildOfType(this, DSMStringLiteralExpr.class);
  }

  @Override
  @Nullable
  public DSMUuidLiteral getUuidLiteral() {
    return PsiTreeUtil.getChildOfType(this, DSMUuidLiteral.class);
  }

}
