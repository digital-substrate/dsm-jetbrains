// This is a generated file. Not intended for manual editing.
package com.digitalsubstrate.dsm.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.digitalsubstrate.dsm.stub.DSMNamedElementStub;

public interface DSMStructDeclaration extends DSMNamedElement, StubBasedPsiElement<DSMNamedElementStub> {

  @NotNull
  List<DSMFieldDeclaration> getFieldDeclarationList();

  @Nullable
  PsiElement getIdentifier();

  //WARNING: getName(...) is skipped
  //matching getName(DSMStructDeclaration, ...)
  //methods are not found in null

  //WARNING: setName(...) is skipped
  //matching setName(DSMStructDeclaration, ...)
  //methods are not found in null

  //WARNING: getNameIdentifier(...) is skipped
  //matching getNameIdentifier(DSMStructDeclaration, ...)
  //methods are not found in null

  //WARNING: getTextOffset(...) is skipped
  //matching getTextOffset(DSMStructDeclaration, ...)
  //methods are not found in null

}
