// This is a generated file. Not intended for manual editing.
package com.digitalsubstrate.dsm.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.digitalsubstrate.dsm.psi.DSMTypes.*;
import com.digitalsubstrate.dsm.psi.*;

public class DSMFunctionPoolDeclarationImpl extends DSMNamedElementImpl implements DSMFunctionPoolDeclaration {

  public DSMFunctionPoolDeclarationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DSMVisitor visitor) {
    visitor.visitFunctionPoolDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DSMVisitor) accept((DSMVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DSMFunctionDeclaration> getFunctionDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DSMFunctionDeclaration.class);
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
