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

public class DSMTypeReferenceImpl extends ASTWrapperPsiElement implements DSMTypeReference {

  public DSMTypeReferenceImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DSMVisitor visitor) {
    visitor.visitTypeReference(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DSMVisitor) accept((DSMVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DSMGenericType getGenericType() {
    return PsiTreeUtil.getChildOfType(this, DSMGenericType.class);
  }

  @Override
  @Nullable
  public DSMPrimitiveType getPrimitiveType() {
    return PsiTreeUtil.getChildOfType(this, DSMPrimitiveType.class);
  }

  @Override
  @Nullable
  public DSMUserTypeReference getUserTypeReference() {
    return PsiTreeUtil.getChildOfType(this, DSMUserTypeReference.class);
  }

}
