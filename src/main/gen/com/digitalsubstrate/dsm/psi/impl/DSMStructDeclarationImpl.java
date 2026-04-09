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
import com.intellij.psi.stubs.IStubElementType;
import com.digitalsubstrate.dsm.stub.DSMNamedElementStub;

public class DSMStructDeclarationImpl extends DSMNamedElementImpl implements DSMStructDeclaration {

  public DSMStructDeclarationImpl(ASTNode node) {
    super(node);
  }

  public DSMStructDeclarationImpl(DSMNamedElementStub stub, IStubElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull DSMVisitor visitor) {
    visitor.visitStructDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DSMVisitor) accept((DSMVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DSMFieldDeclaration> getFieldDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DSMFieldDeclaration.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

}
