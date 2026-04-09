// This is a generated file. Not intended for manual editing.
package com.digitalsubstrate.dsm.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DSMFieldDeclaration extends PsiElement {

  @Nullable
  DSMDefaultValue getDefaultValue();

  @NotNull
  DSMIdentifierOrKeyword getIdentifierOrKeyword();

  @NotNull
  DSMTypeReference getTypeReference();

}
