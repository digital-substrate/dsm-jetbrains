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

public class DSMNamespaceDeclarationImpl extends ASTWrapperPsiElement implements DSMNamespaceDeclaration {

  public DSMNamespaceDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DSMVisitor visitor) {
    visitor.visitNamespaceDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DSMVisitor) accept((DSMVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DSMNamespaceBody getNamespaceBody() {
    return PsiTreeUtil.getChildOfType(this, DSMNamespaceBody.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @Nullable
  public PsiElement getUuid() {
    return findChildByType(UUID);
  }

}
