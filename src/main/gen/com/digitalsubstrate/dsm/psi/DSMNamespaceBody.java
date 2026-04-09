// This is a generated file. Not intended for manual editing.
package com.digitalsubstrate.dsm.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DSMNamespaceBody extends PsiElement {

  @NotNull
  List<DSMAttachmentDeclaration> getAttachmentDeclarationList();

  @NotNull
  List<DSMClubDeclaration> getClubDeclarationList();

  @NotNull
  List<DSMConceptDeclaration> getConceptDeclarationList();

  @NotNull
  List<DSMEnumDeclaration> getEnumDeclarationList();

  @NotNull
  List<DSMMembershipDeclaration> getMembershipDeclarationList();

  @NotNull
  List<DSMStructDeclaration> getStructDeclarationList();

}
