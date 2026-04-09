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

public class DSMNamespaceBodyImpl extends ASTWrapperPsiElement implements DSMNamespaceBody {

  public DSMNamespaceBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DSMVisitor visitor) {
    visitor.visitNamespaceBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DSMVisitor) accept((DSMVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DSMAttachmentDeclaration> getAttachmentDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DSMAttachmentDeclaration.class);
  }

  @Override
  @NotNull
  public List<DSMClubDeclaration> getClubDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DSMClubDeclaration.class);
  }

  @Override
  @NotNull
  public List<DSMConceptDeclaration> getConceptDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DSMConceptDeclaration.class);
  }

  @Override
  @NotNull
  public List<DSMEnumDeclaration> getEnumDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DSMEnumDeclaration.class);
  }

  @Override
  @NotNull
  public List<DSMMembershipDeclaration> getMembershipDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DSMMembershipDeclaration.class);
  }

  @Override
  @NotNull
  public List<DSMStructDeclaration> getStructDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DSMStructDeclaration.class);
  }

}
