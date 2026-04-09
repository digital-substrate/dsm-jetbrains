# BNF-Driven Architecture: From Grammar to IDE Features

**Purpose:** Comprendre l'architecture IntelliJ Platform avec une approche progressive bottom-up
**Audience:** Développeur découvrant IntelliJ Platform et Grammar-Kit
**Approach:** Progressive disclosure - partir de la fondation simple vers le système complet
**Version:** 1.0
**Date:** 2025-11-19

---

## Introduction: La Vision d'Ensemble

**Question centrale:** Comment un simple fichier `.bnf` devient-il un plugin IDE complet avec syntax highlighting, navigation, completion, etc.?

**Réponse courte:** Architecture BNF-Driven

```
DSM.bnf (Grammaire étendue - 400 lignes)
    ↓ Grammar-Kit transforme
Code généré (Parser + PSI - 5000+ lignes)
    ↓ S'intègre dans
Extension Points (La machinerie IntelliJ - 21 points d'extension)
    ↓ Résultat
Plugin IDE complet (syntax highlighting, navigation, completion, etc.)
```

**Ce document suit une approche progressive:** Chaque niveau construit sur le précédent, dévoilant progressivement la complexité.

**Niveaux:**
- **Niveau 0:** La fondation - Grammaire BNF étendue (simple)
- **Niveau 1:** Le transformateur - Grammar-Kit (génération de code)
- **Niveau 2:** Les pièces - Code généré (parser, AST, PSI)
- **Niveau 3:** L'exécution - Runtime (comment les pièces fonctionnent)
- **Niveau 4:** La machinerie - Extension Points (intégration IDE)
- **Niveau 5:** Le système complet - De la grammaire à l'IDE

---

## Niveau 0: La Fondation - Grammaire BNF Étendue

### Qu'est-ce qu'on écrit?

**Un fichier DSM.bnf (400 lignes) qui déclare:**

1. **La syntaxe du langage DSM** (partie BNF classique)
2. **L'architecture PSI** (métadonnées IntelliJ Platform)
3. **Les patterns** (mixins, error recovery)

**Exemple concret - Règle pour un concept:**

```bnf
// Ligne 117-126 de DSM.bnf
conceptDeclaration ::=
    DOC_STRING?
    CONCEPT IDENTIFIER conceptInheritance? SEMICOLON
    {
        pin=2
        recoverWhile=conceptRecover
        mixin="com.digitalsubstrate.dsm.psi.impl.DSMNamedElementImpl"
        implements="com.digitalsubstrate.dsm.psi.DSMNamedElement"
        methods=[getName setName getNameIdentifier getTextOffset]
    }

private conceptRecover ::= !(CONCEPT | STRUCT | ENUM | CLUB | MEMBERSHIP | ATTACHMENT | RBRACE)

conceptInheritance ::= IS_A IDENTIFIER
```

### Décortiquons ligne par ligne

**Partie 1: Syntaxe (BNF classique)**
```bnf
conceptDeclaration ::=
    DOC_STRING?                // Documentation optionnelle
    CONCEPT                    // Mot-clé "concept"
    IDENTIFIER                 // Nom du concept
    conceptInheritance?        // "is a Parent" optionnel
    SEMICOLON                  // Point-virgule terminal
```

**Signifie:** Un concept DSM ressemble à `concept Vehicle;` ou `concept Car is a Vehicle;`

**Partie 2: Métadonnées IntelliJ Platform**
```bnf
{
    pin=2                      // Error recovery: commit après CONCEPT
    recoverWhile=conceptRecover // Comment récupérer des erreurs
    mixin="...DSMNamedElementImpl"  // Classe de base (comportement partagé)
    implements="...DSMNamedElement" // Interface (contrat)
    methods=[getName setName ...]   // Méthodes custom
}
```

**Signifie:** Instructions pour Grammar-Kit sur comment générer le code

### Que déclare-t-on vraiment?

**Sans le savoir, en écrivant cette règle BNF, tu déclares:**

1. **Un type PSI** - DSMConceptDeclaration
2. **Une interface** - DSMConceptDeclaration extends DSMNamedElement
3. **Une implémentation** - DSMConceptDeclarationImpl extends DSMNamedElementImpl
4. **Des méthodes de navigation** - getIdentifier(), getConceptInheritance()
5. **Un comportement de parsing** - Reconnaître "concept Foo;"
6. **Un mécanisme d'error recovery** - Comment gérer les erreurs
7. **Des méthodes métier** - getName(), setName()

**7 déclarations en une seule règle!**

### Les attributs BNF étendus

| Attribut | Rôle | Exemple | Effet |
|----------|------|---------|-------|
| `pin` | Error recovery | `pin=2` | Commit après 2ème élément (IDENTIFIER) |
| `recoverWhile` | Récupération erreur | `recoverWhile=conceptRecover` | Skip tokens jusqu'à prochain concept/struct/etc. |
| `mixin` | Classe de base | `mixin="...DSMNamedElementImpl"` | Impl hérite de cette classe (partage getName/setName) |
| `implements` | Interface | `implements="...DSMNamedElement"` | Interface étend cette interface |
| `methods` | Méthodes custom | `methods=[getName setName]` | Ajoutées à l'interface |
| `extends` | Hiérarchie PSI | `extends=baseRule` | Héritage entre types PSI |

**Ces attributs n'existent pas dans une BNF standard** - ils sont spécifiques à Grammar-Kit pour IntelliJ Platform.

### Résumé Niveau 0

✅ **Fondation:** Un fichier DSM.bnf qui déclare la grammaire + métadonnées
✅ **Simple à écrire:** Syntaxe déclarative, pas de code impératif
✅ **Puissant:** Une règle déclare structure syntaxique + architecture PSI + comportements
✅ **Source de vérité:** TOUT découle de ce fichier

**Prochaine étape:** Comment Grammar-Kit transforme cette déclaration en code?

---

## Niveau 1: Le Transformateur - Grammar-Kit

### Qu'est-ce que Grammar-Kit?

**Grammar-Kit = Compilateur d'architecture**

```
Input:  DSM.bnf (grammaire étendue)
Output: Parser + PSI classes + Interfaces + Visitors
```

**Ce n'est pas juste un générateur de parser** - c'est un générateur d'architecture complète intégrée à IntelliJ Platform.

### Commande de génération

```bash
./gradlew generateParser
```

**Cette commande unique:**
1. Lit DSM.bnf
2. Analyse les règles et métadonnées
3. Génère ~5000 lignes de code Java
4. Place les fichiers dans src/main/gen/

### Que génère Grammar-Kit exactement?

**Pour la règle `conceptDeclaration` (Niveau 0), Grammar-Kit génère:**

#### 1. Interface PSI (Contrat)

**Fichier:** `gen/psi/DSMConceptDeclaration.java`

```java
public interface DSMConceptDeclaration extends DSMNamedElement {
    // Chaque élément de la règle BNF → méthode getter
    @Nullable PsiElement getDocString();           // DOC_STRING?
    @Nullable PsiElement getConcept();             // CONCEPT
    @Nullable PsiElement getIdentifier();          // IDENTIFIER
    @Nullable DSMConceptInheritance getConceptInheritance();  // conceptInheritance?
    @Nullable PsiElement getSemicolon();           // SEMICOLON

    // Méthodes de l'attribut methods=[...]
    String getName();
    PsiElement setName(String name);
    PsiElement getNameIdentifier();
    int getTextOffset();
}
```

**Règle de génération:**
- Chaque token/sous-règle dans la définition BNF → Méthode `getXxx()`
- Optionnel (`?`) → `@Nullable`
- Répétition (`*` ou `+`) → `List<T>`

#### 2. Implémentation PSI (Classe concrète)

**Fichier:** `gen/psi/impl/DSMConceptDeclarationImpl.java`

```java
public class DSMConceptDeclarationImpl extends DSMNamedElementImpl  // mixin
                                       implements DSMConceptDeclaration {

    public DSMConceptDeclarationImpl(@NotNull ASTNode node) {
        super(node);  // Passe l'ASTNode au parent
    }

    @Override
    public PsiElement getDocString() {
        return findChildByType(DOC_STRING);  // Cherche dans l'arbre AST
    }

    @Override
    public PsiElement getIdentifier() {
        return findChildByType(IDENTIFIER);
    }

    @Override
    public DSMConceptInheritance getConceptInheritance() {
        return findChildByClass(DSMConceptInheritance.class);
    }

    // getName(), setName(), etc. hérités de DSMNamedElementImpl (mixin)

    @Override
    public void accept(@NotNull DSMVisitor visitor) {
        visitor.visitConceptDeclaration(this);  // Visitor pattern
    }
}
```

**Détails importants:**
- Hérite du mixin spécifié (`DSMNamedElementImpl`)
- Wraps un `ASTNode` (stockage bas niveau)
- Chaque méthode cherche dans l'arbre AST
- Supporte le visitor pattern

#### 3. Parser (Reconnaissance syntaxique)

**Fichier:** `gen/parser/DSMParser.java`

```java
public class DSMParser {
    // Méthode générée pour la règle conceptDeclaration
    public static boolean conceptDeclaration(PsiBuilder builder, int level) {
        PsiBuilder.Marker marker = builder.mark();

        // DOC_STRING?
        if (builder.getTokenType() == DOC_STRING) {
            builder.advanceLexer();
        }

        // CONCEPT (pin=2 commence ici)
        if (builder.getTokenType() != CONCEPT) {
            marker.rollbackTo();
            return false;
        }
        builder.advanceLexer();

        // IDENTIFIER (pin=2 - si on arrive ici, commit!)
        if (builder.getTokenType() != IDENTIFIER) {
            // Erreur, mais on continue (pin committed)
            builder.error("Expected identifier");
        } else {
            builder.advanceLexer();
        }

        // conceptInheritance? (optionnel)
        conceptInheritance(builder, level + 1);

        // SEMICOLON
        if (builder.getTokenType() != SEMICOLON) {
            // Erreur - utilise recoverWhile
            recoverConceptDeclaration(builder);
        }

        // Marque comme CONCEPT_DECLARATION dans l'AST
        marker.done(CONCEPT_DECLARATION);
        return true;
    }
}
```

**Logique générée:**
- Vérifie chaque élément de la règle dans l'ordre
- `pin=2` → Commit après 2ème élément
- `recoverWhile` → Récupération en cas d'erreur
- Construit l'arbre AST (via PsiBuilder markers)

#### 4. Types d'éléments (Constants)

**Fichier:** `gen/psi/DSMTypes.java`

```java
public interface DSMTypes {
    IElementType CONCEPT_DECLARATION = new DSMElementType("CONCEPT_DECLARATION");
    IElementType CONCEPT_INHERITANCE = new DSMElementType("CONCEPT_INHERITANCE");

    IElementType CONCEPT = new DSMTokenType("concept");
    IElementType IDENTIFIER = new DSMTokenType("IDENTIFIER");
    IElementType SEMICOLON = new DSMTokenType(";");
    IElementType DOC_STRING = new DSMTokenType("DOC_STRING");
    // ... tous les tokens et types d'éléments
}
```

**Utilisé pour:**
- Identifier les types de nœuds AST
- Navigation dans l'arbre PSI
- Pattern matching sur les éléments

#### 5. Visitor (Pattern de traversée)

**Fichier:** `gen/psi/DSMVisitor.java`

```java
public class DSMVisitor extends PsiElementVisitor {
    public void visitConceptDeclaration(@NotNull DSMConceptDeclaration o) {
        visitNamedElement(o);  // Remonte vers l'interface parent
    }

    public void visitNamedElement(@NotNull DSMNamedElement o) {
        visitElement(o);
    }

    public void visitElement(@NotNull PsiElement o) {
        super.visitElement(o);
    }
}
```

**Hiérarchie respectée:**
- DSMConceptDeclaration implements DSMNamedElement
- visitConceptDeclaration() appelle visitNamedElement()
- Pattern standard de traversée d'arbre

### Mapping complet: BNF → Code généré

**Une règle BNF:**
```bnf
conceptDeclaration ::=
    DOC_STRING? CONCEPT IDENTIFIER conceptInheritance? SEMICOLON
    {mixin="..." implements="..." methods=[...]}
```

**Génère automatiquement:**

| Ce qui est généré | Fichier | Lignes | Responsabilité |
|-------------------|---------|--------|----------------|
| Interface PSI | DSMConceptDeclaration.java | ~30 | Contrat public, méthodes de navigation |
| Implémentation PSI | DSMConceptDeclarationImpl.java | ~60 | Wrapper AST, délégation |
| Méthode parser | DSMParser.conceptDeclaration() | ~50 | Reconnaissance syntaxique, construction AST |
| Type d'élément | DSMTypes.CONCEPT_DECLARATION | 1 | Identification du type de nœud |
| Méthode visitor | DSMVisitor.visitConceptDeclaration() | ~5 | Traversée d'arbre |

**Total: ~150 lignes générées pour une règle BNF de 10 lignes!**

### Résumé Niveau 1

✅ **Grammar-Kit = Compilateur:** Transforme déclarations → code impératif
✅ **Génération massive:** 1 règle BNF → ~150 lignes de code Java
✅ **Cohérence garantie:** Architecture uniforme pour toutes les règles
✅ **Intégration Platform:** Code généré compatible IntelliJ Platform

**Prochaine étape:** Comment le code généré s'exécute à runtime?

---

## Niveau 2: Les Pièces - Code Généré et Responsabilités

### Vue d'ensemble des pièces générées

**Grammar-Kit génère 5 types de pièces qui travaillent ensemble:**

```
Parser ──────> Construit ──────> AST
                                  ↓
                              Wrappé par
                                  ↓
Interfaces <─── Implémentent <── Implémentations PSI
    ↑                                ↓
    │                            Utilisent
    │                                ↓
    └──────────────────────────── Visitors
```

### Pièce 1: Le Parser (DSMParser.java)

**Responsabilité:** Transformer texte → arbre AST

**Input:** Flux de tokens du lexer
```
CONCEPT, IDENTIFIER("Vehicle"), SEMICOLON
```

**Output:** Arbre AST
```
ASTNode(CONCEPT_DECLARATION)
  ├─ ASTNode(CONCEPT)
  ├─ ASTNode(IDENTIFIER, "Vehicle")
  └─ ASTNode(SEMICOLON)
```

**Fonctionnement:**
```java
public static boolean conceptDeclaration(PsiBuilder builder, int level) {
    PsiBuilder.Marker marker = builder.mark();  // Commence un nœud

    // Vérifie et consomme les tokens
    if (!consumeToken(builder, CONCEPT)) return false;
    if (!consumeToken(builder, IDENTIFIER)) return false;
    // ...

    marker.done(CONCEPT_DECLARATION);  // Finalise le nœud AST
    return true;
}
```

**Caractéristiques:**
- Parsing descendant récursif (PEG)
- Error recovery avec `pin` et `recoverWhile`
- Construit l'AST via PsiBuilder markers
- Pas d'interprétation sémantique (juste syntaxe)

### Pièce 2: L'AST (Abstract Syntax Tree)

**Responsabilité:** Stockage immuable de la structure

**Type:** `ASTNode` (interface IntelliJ Platform)

**Exemple:**
```java
ASTNode conceptNode = ...;
conceptNode.getElementType();  // CONCEPT_DECLARATION
conceptNode.getText();         // "concept Vehicle;"
conceptNode.getTextRange();    // [0, 16]
conceptNode.getFirstChildNode();  // ASTNode(CONCEPT)
```

**Caractéristiques:**
- Immuable (thread-safe)
- Stocke tous les tokens (y compris whitespace)
- Positions exactes (offsets)
- Bas niveau (pas de getName(), etc.)

### Pièce 3: Les Interfaces PSI (DSMConceptDeclaration.java)

**Responsabilité:** Contrat public pour accès haut niveau

**Exemple:**
```java
public interface DSMConceptDeclaration extends DSMNamedElement {
    @Nullable PsiElement getIdentifier();
    @Nullable DSMConceptInheritance getConceptInheritance();

    // Hérité de DSMNamedElement
    String getName();
    PsiElement setName(String name);
}
```

**Caractéristiques:**
- Contrat stable (ne change pas souvent)
- API haut niveau (getName vs getIdentifier().getText())
- Polymorphisme (DSMNamedElement = concept, struct, enum)
- Pas d'implémentation (juste le contrat)

### Pièce 4: Les Implémentations PSI (DSMConceptDeclarationImpl.java)

**Responsabilité:** Wrapper AST → API PSI

**Exemple:**
```java
public class DSMConceptDeclarationImpl extends DSMNamedElementImpl
                                       implements DSMConceptDeclaration {
    private final ASTNode myNode;  // Référence à l'AST

    public DSMConceptDeclarationImpl(ASTNode node) {
        super(node);
        this.myNode = node;
    }

    @Override
    public PsiElement getIdentifier() {
        return myNode.findChildByType(IDENTIFIER)?.getPsi();
        // Cherche dans AST → Convertit en PSI
    }

    @Override
    public String getName() {
        return getIdentifier()?.getText();  // Délègue au mixin
    }
}
```

**Caractéristiques:**
- Wraps un ASTNode
- Délègue les opérations bas niveau à l'AST
- Hérite du mixin (DSMNamedElementImpl)
- Créé à la demande (lazy loading)

### Pièce 5: Les Visitors (DSMVisitor.java)

**Responsabilité:** Traversée d'arbre standardisée

**Exemple:**
```java
public class DSMVisitor extends PsiElementVisitor {
    public void visitConceptDeclaration(@NotNull DSMConceptDeclaration o) {
        visitNamedElement(o);  // Remonte vers parent
    }

    public void visitNamedElement(@NotNull DSMNamedElement o) {
        visitElement(o);  // Remonte encore
    }
}
```

**Utilisation:**
```kotlin
class ConceptCollector : DSMVisitor() {
    val concepts = mutableListOf<DSMConceptDeclaration>()

    override fun visitConceptDeclaration(o: DSMConceptDeclaration) {
        concepts.add(o)
        super.visitConceptDeclaration(o)  // Continue traversée
    }
}

// Usage
val collector = ConceptCollector()
file.accept(collector)
println("Found ${collector.concepts.size} concepts")
```

**Caractéristiques:**
- Pattern standard de traversée
- Respecte la hiérarchie PSI
- Évite la récursion manuelle
- Extensible (override les méthodes visitXxx)

### Comment les pièces collaborent

**Scénario: User ouvre un fichier DSM**

```dsm
concept Vehicle;
```

**Étape 1: Lexing (pas généré par Grammar-Kit)**
```
Lexer lit le texte caractère par caractère
    → Produit tokens: [CONCEPT, IDENTIFIER("Vehicle"), SEMICOLON]
```

**Étape 2: Parsing (Pièce 1 - Parser)**
```
DSMParser.conceptDeclaration(builder, 0)
    → Vérifie CONCEPT ✓
    → Vérifie IDENTIFIER ✓
    → Vérifie SEMICOLON ✓
    → Marque comme CONCEPT_DECLARATION
    → Construit ASTNode(CONCEPT_DECLARATION)
```

**Étape 3: AST créé (Pièce 2)**
```
ASTNode(CONCEPT_DECLARATION) [offset: 0-16]
  ├─ ASTNode(CONCEPT, "concept") [0-7]
  ├─ ASTNode(IDENTIFIER, "Vehicle") [8-15]
  └─ ASTNode(SEMICOLON, ";") [15-16]
```

**Étape 4: User navigue (Ctrl+Click sur "Vehicle")**
```
Platform:
    file.findElementAt(8)  // Offset du "V"
        ↓
    Cherche dans AST à offset 8
        ↓
    Trouve: ASTNode(IDENTIFIER, "Vehicle")
        ↓
    Appelle: astNode.getPsi()
        ↓
    Crée wrapper PSI: PsiElement("Vehicle")
```

**Étape 5: PSI créé (Pièce 3 + 4)**
```
DSMConceptDeclarationImpl créé
    → Wraps ASTNode(CONCEPT_DECLARATION)
    → Implémente DSMConceptDeclaration (interface)
    → Hérite de DSMNamedElementImpl (mixin)
```

**Étape 6: Ton code utilise**
```kotlin
val concept: DSMConceptDeclaration = ...  // Interface (Pièce 3)
val name = concept.name                   // Mixin DSMNamedElementImpl
val identifier = concept.identifier       // Implémentation (Pièce 4)
```

### Tableau récapitulatif des pièces

| Pièce | Type | Responsabilité | Créé quand | Lifetime |
|-------|------|----------------|------------|----------|
| **Parser** | Stateless | Texte → AST | À chaque parse | Exécution courte |
| **AST** | ASTNode | Stockage structure | Parse du fichier | Tant que fichier ouvert |
| **Interface PSI** | Java interface | Contrat public | Compile-time | Permanent (code source) |
| **Impl PSI** | Java class | Wrapper AST→PSI | Lazy (navigation) | Jusqu'à invalidation |
| **Visitor** | Java class | Traversée arbre | Compile-time | Permanent (code source) |

### Résumé Niveau 2

✅ **5 pièces complémentaires:** Parser, AST, Interfaces, Implémentations, Visitors
✅ **Chaque pièce a une responsabilité claire:** Pas de chevauchement
✅ **Collaboration fluide:** Les pièces s'assemblent naturellement
✅ **Architecture cohérente:** Même pattern pour toutes les règles BNF

**Prochaine étape:** Comment ces pièces s'exécutent à runtime?

---

## Niveau 3: L'Exécution - Runtime

### Le cycle de vie complet

**De l'ouverture du fichier à l'utilisation par ton code:**

```
1. User ouvre fichier.dsm
    ↓
2. Platform demande le parse
    ↓
3. Lexer → Tokens
    ↓
4. Parser → AST (toujours créé)
    ↓
5. AST existe en mémoire
    ↓
6. User navigue (Ctrl+Click, hover, etc.)
    ↓
7. Platform demande PSI pour un élément
    ↓
8. PSI wrapper créé (lazy) → Wraps ASTNode
    ↓
9. Extension point appelle méthode PSI
    ↓
10. Ton code métier s'exécute
```

### Phase 1: Parsing (Toujours exécuté)

**Déclencheur:** User ouvre un fichier `.dsm`

**Flux:**
```
Platform IntelliJ:
    Détecte extension .dsm
        ↓
    Cherche FileType enregistré (extension point: fileType)
        ↓
    Trouve: DSMFileType
        ↓
    Obtient le Language: DSM
        ↓
    Cherche ParserDefinition pour DSM
        ↓
    Trouve: DSMParserDefinition
        ↓
    Appelle: createParser()
        ↓
    Obtient: DSMParser (code généré)
        ↓
DSMParser:
    Reçoit flux de tokens du lexer
        ↓
    Exécute les méthodes de parsing (conceptDeclaration, structDeclaration, etc.)
        ↓
    Construit l'arbre AST (via PsiBuilder)
        ↓
    Retourne: ASTNode racine (DSMFile)
```

**Résultat:** AST complet en mémoire

**Caractéristiques:**
- Exécuté UNE fois à l'ouverture
- Reparse incrémental si édition
- AST garde tous les tokens (y compris whitespace/comments)
- Thread-safe (immuable)

### Phase 2: Lazy PSI Creation (À la demande)

**Déclencheur:** Platform ou extension point a besoin d'un PSI élément

**Exemples de déclencheurs:**
- User Ctrl+Click → Platform cherche la référence
- User hover → Platform demande documentation
- Syntax highlighting → Annotator traverse le fichier
- Structure view → Platform construit la hiérarchie

**Flux:**
```
Extension point:
    Appelle file.findElementAt(offset)
        ↓
Platform:
    Cherche dans l'AST à cet offset
        ↓
    Trouve: ASTNode(IDENTIFIER, "Vehicle")
        ↓
    Vérifie: PSI wrapper existe déjà?
        ↓
    Non → Crée wrapper:
        astNode.getPsi()
            ↓
        Détermine le type: CONCEPT_DECLARATION
            ↓
        Instancie: new DSMConceptDeclarationImpl(astNode)
            ↓
        Cache dans astNode
            ↓
    Retourne: DSMConceptDeclaration (interface)
```

**Résultat:** PSI wrapper prêt à utiliser

**Caractéristiques:**
- Lazy loading (créé seulement si demandé)
- Caché dans l'ASTNode
- Invalidé si AST change
- API haut niveau (getName, getReference, etc.)

### Phase 3: Utilisation par Extension Points

**C'est ici que la magie opère** - Les extension points utilisent les PSI générés.

**Exemple 1: Syntax Highlighting (Annotator)**

**Extension point:** `<annotator>`

**Flux:**
```
Platform:
    Fichier ouvert → Besoin de highlighting
        ↓
    Cherche annotators pour language="DSM"
        ↓
    Trouve: DSMSemanticHighlightingAnnotator
        ↓
    Pour chaque élément PSI dans le fichier:
        Appelle: annotator.annotate(element, holder)
            ↓
Ton annotator:
    if (element is DSMUserTypeReference) {
        holder.newAnnotation(...)
            .textAttributes(USER_TYPE_COLOR)
            .create()
    }
```

**PSI utilisé:** L'annotator reçoit des éléments PSI (DSMUserTypeReference, etc.)

**Exemple 2: Documentation (DocumentationProvider)**

**Extension point:** `<lang.documentationProvider>`

**Flux:**
```
User:
    Hover sur "Vehicle"
        ↓
Platform:
    findElementAt(offset) → Obtient PSI element
        ↓
    Cherche documentationProvider pour language="DSM"
        ↓
    Trouve: DSMDocumentationProvider
        ↓
    Appelle: provider.generateDoc(element, originalElement)
        ↓
Ton provider:
    when (element) {
        is DSMConceptDeclaration -> {
            val name = element.name              // ← PSI API
            val doc = element.docString?.text    // ← PSI API
            return "<html><b>$name</b><p>$doc</p></html>"
        }
    }
```

**PSI utilisé:** element est un DSMConceptDeclaration (interface générée)

**Exemple 3: Reference Resolution (PsiReference)**

**Extension point:** Indirect - via `element.getReference()`

**Flux:**
```
User:
    Ctrl+Click sur "Vehicle" (dans "is a Vehicle")
        ↓
Platform:
    findElementAt(offset) → Obtient element (DSMUserTypeReference)
        ↓
    Appelle: element.getReference()
        ↓
    Obtient: DSMReference (ton code)
        ↓
    Appelle: reference.resolve()
        ↓
Ton code:
    override fun resolve(): PsiElement? {
        val name = element.text  // "Vehicle"

        // Cherche dans le fichier
        val concepts = PsiTreeUtil.collectElementsOfType(
            file,
            DSMConceptDeclaration::class.java  // ← Interface générée
        )

        return concepts.find { it.name == name }  // ← PSI API (getName)
    }
```

**PSI utilisé:** DSMConceptDeclaration (interface générée) avec API getName()

### Invalidation et Reparse

**Scénario: User édite le fichier**

```
User:
    Type 's' → "concept Vehicles;"
        ↓
Document:
    Texte modifié dans l'éditeur
        ↓
Platform (PsiDocumentManager):
    Détecte changement → Queue reparse
        ↓
    Reparse incrémental:
        Analyse zone modifiée
            ↓
        Modifie l'AST (minimal changes)
            ↓
        Invalide PSI wrappers affectés
            ↓
        Notifie listeners (PsiTreeChangeEvent)
            ↓
    PSI sera recréé à la prochaine demande (lazy)
```

**Caractéristiques:**
- Reparse incrémental (pas tout le fichier)
- AST modifié (mutations minimales)
- PSI invalidé (sera recréé)
- Performance optimisée

### Threading Model

**Règles strictes IntelliJ Platform:**

```
PSI reads → ReadAction (any thread + read lock)
PSI writes → WriteAction (EDT + write lock)
UI updates → EDT only
```

**Exemple:**
```kotlin
// Lecture PSI (background thread OK avec read lock)
fun getAllConcepts(file: DSMFile): List<DSMConceptDeclaration> {
    return ReadAction.compute<List<DSMConceptDeclaration>, Throwable> {
        PsiTreeUtil.collectElementsOfType(file, DSMConceptDeclaration::class.java).toList()
    }
}

// Modification PSI (EDT + write lock requis)
fun renameConcept(concept: DSMConceptDeclaration, newName: String) {
    WriteAction.run<Throwable> {
        concept.setName(newName)  // Modifie l'AST
    }
}
```

### Résumé Niveau 3

✅ **3 phases:** Parsing (toujours) → PSI creation (lazy) → Utilisation (extension points)
✅ **AST = toujours en mémoire:** Source de vérité
✅ **PSI = créé à la demande:** Wrapper léger, invalidable
✅ **Extension points = consommateurs:** Utilisent l'API PSI générée
✅ **Threading model strict:** Read/Write actions requises

**Prochaine étape:** La machinerie - Comment les extension points s'intègrent?

---

## Niveau 4: La Machinerie - Extension Points

### Qu'est-ce que la machinerie?

**La machinerie = Les 21 extension points de ton plugin DSM**

Chaque extension point:
- A une **responsabilité précise** (syntax highlighting, navigation, etc.)
- **Consomme** les PSI générés par Grammar-Kit
- **Fournit** une feature IDE

**Architecture:**
```
Code généré (PSI)
    ↓ Utilisé par
Extension Points (machinerie)
    ↓ Résultat
Features IDE (syntax highlighting, completion, navigation, etc.)
```

### Les 21 Extension Points du plugin DSM

**Rappel:** Voir `.claude/platform-architecture-guide.md` pour le catalogue complet.

**Regroupement par catégorie:**

#### Catégorie 1: Language Definition (4 extension points)

**Responsabilité:** Définir DSM comme un langage IntelliJ

| Extension Point | Responsabilité | PSI utilisé |
|-----------------|----------------|-------------|
| `fileType` | Associer .dsm → DSM language | DSMFileType |
| `lang.parserDefinition` | Fournir le parser | DSMParser (généré) |
| `lang.elementManipulator` | Renommer éléments | DSMUserTypeReference |
| `colorSettingsPage` | UI config couleurs | N/A (UI seulement) |

**Comment ça marche:**

```xml
<!-- plugin.xml -->
<fileType name="DSM File"
          implementationClass="com.digitalsubstrate.dsm.core.DSMFileType"
          language="DSM"
          extensions="dsm"/>
```

```kotlin
// DSMFileType.kt
object DSMFileType : LanguageFileType(DSMLanguage.INSTANCE) {
    override fun getName() = "DSM File"
    override fun getDescription() = "Digital Substrate Model file"
    override fun getDefaultExtension() = "dsm"
    override fun getIcon() = DSMIcons.FILE
}
```

**Résultat:** Platform reconnaît les fichiers `.dsm` comme fichiers DSM.

#### Catégorie 2: Syntax Highlighting (2 extension points)

**Responsabilité:** Colorer le code

| Extension Point | Responsabilité | PSI utilisé |
|-----------------|----------------|-------------|
| `lang.syntaxHighlighterFactory` | Highlighting lexical (tokens) | Tokens (CONCEPT, IDENTIFIER, etc.) |
| `annotator` | Highlighting sémantique (types) | DSMUserTypeReference, DSMConceptDeclaration, etc. |

**Flux:**

```
Fichier ouvert
    ↓
lang.syntaxHighlighterFactory (lexical - rapide)
    Colore: keywords (concept, struct), strings, comments
        ↓
annotator (sémantique - après parse)
    Parcourt PSI:
        if (element is DSMUserTypeReference) {
            holder.newAnnotation(...)
                .textAttributes(USER_TYPE_COLOR)
        }
```

**PSI utilisé:**
```kotlin
class DSMSemanticHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is DSMUserTypeReference -> {  // ← Interface PSI générée
                holder.newAnnotation(...)
                    .range(element.textRange)
                    .textAttributes(DSMSyntaxHighlighter.USER_TYPE)
                    .create()
            }
        }
    }
}
```

**Résultat:** Code coloré dans l'éditeur.

#### Catégorie 3: Code Intelligence (3 extension points)

**Responsabilité:** Completion, navigation, find usages

| Extension Point | Responsabilité | PSI utilisé |
|-----------------|----------------|-------------|
| `completion.contributor` | Auto-completion (Ctrl+Space) | DSMFile, contexte PSI |
| `lang.findUsagesProvider` | Find usages (Alt+F7) | DSMNamedElement |
| `gotoSymbolContributor` | Symbol search (Cmd+O) | DSMConceptDeclaration, DSMStructDeclaration, etc. |

**Exemple: Symbol Search**

```xml
<gotoSymbolContributor implementation="...DSMChooseByNameContributor"/>
```

```kotlin
class DSMChooseByNameContributor : ChooseByNameContributor {
    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        // Collecte tous les noms de concepts, structs, enums
        val names = mutableListOf<String>()

        FilenameIndex.getAllFilesByExt(project, "dsm").forEach { file ->
            val psiFile = PsiManager.getInstance(project).findFile(file) as? DSMFile ?: return@forEach

            // Collecte concepts
            PsiTreeUtil.collectElementsOfType(psiFile, DSMConceptDeclaration::class.java)
                .forEach { concept ->
                    concept.name?.let { names.add(it) }  // ← PSI API getName()
                }

            // Collecte structs
            PsiTreeUtil.collectElementsOfType(psiFile, DSMStructDeclaration::class.java)
                .forEach { struct ->
                    struct.name?.let { names.add(it) }  // ← PSI API getName()
                }
        }

        return names.toTypedArray()
    }

    override fun getItemsByName(...): Array<NavigationItem> {
        // Retourne les PSI elements (DSMConceptDeclaration, etc.)
    }
}
```

**PSI utilisé:**
- `DSMConceptDeclaration.getName()` (du mixin DSMNamedElementImpl)
- `DSMStructDeclaration.getName()` (du mixin DSMNamedElementImpl)

**Résultat:** User peut faire Cmd+O, taper "Vehi", et trouver "Vehicle".

#### Catégorie 4: Editor Features (6 extension points)

**Responsabilité:** Formatting, folding, documentation, etc.

| Extension Point | Responsabilité | PSI utilisé |
|-----------------|----------------|-------------|
| `lang.formatter` | Code formatting (Cmd+Alt+L) | Tous les éléments PSI |
| `langCodeStyleSettingsProvider` | Settings UI | N/A |
| `lang.braceMatcher` | Highlight matching braces | Tokens ({}, [], <>, ()) |
| `lang.commenter` | Comment/uncomment (Cmd+/) | N/A |
| `lang.foldingBuilder` | Code folding regions | DSMNamespaceDeclaration, etc. |
| `lang.documentationProvider` | Quick doc (F1/Ctrl+Q) | DSMConceptDeclaration, etc. |

**Exemple: Documentation Provider (P0 debt)**

```kotlin
class DSMDocumentationProvider : DocumentationProvider {
    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return when (element) {
            is DSMConceptDeclaration -> {  // ← Interface PSI générée
                val name = element.name ?: return null  // ← PSI API
                val doc = element.docString?.text      // ← PSI API (getDocString)
                val inheritance = element.conceptInheritance?.identifier?.text  // ← PSI API

                buildString {
                    append("<html><body>")
                    append("<b>Concept:</b> $name<br>")
                    if (inheritance != null) {
                        append("<b>Inherits:</b> $inheritance<br>")
                    }
                    if (doc != null) {
                        append("<p>$doc</p>")
                    }
                    append("</body></html>")
                }
            }
            is DSMStructDeclaration -> {  // ← Interface PSI générée
                // Même pattern
            }
            else -> null
        }
    }
}
```

**PSI utilisé:**
- `DSMConceptDeclaration.getName()` (mixin)
- `DSMConceptDeclaration.getDocString()` (généré)
- `DSMConceptDeclaration.getConceptInheritance()` (généré)

**Résultat:** User hover sur "Vehicle" → Documentation popup.

#### Catégorie 5: Structure & Navigation (1 extension point)

**Responsabilité:** Structure panel (Cmd+7)

| Extension Point | Responsabilité | PSI utilisé |
|-----------------|----------------|-------------|
| `lang.psiStructureViewFactory` | Structure tree view | DSMNamespaceDeclaration, DSMConceptDeclaration, etc. |

**Flux:**

```kotlin
class DSMPsiStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder? {
        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(...): StructureViewModel {
                return DSMStructureViewModel(psiFile)
            }
        }
    }
}

class DSMStructureViewModel(psiFile: PsiFile) : ... {
    override fun getRoot(): StructureViewTreeElement {
        return DSMStructureViewElement(psiFile)  // Racine
    }
}

class DSMStructureViewElement(private val element: PsiElement) : StructureViewTreeElement {
    override fun getChildren(): Array<TreeElement> {
        return when (element) {
            is DSMFile -> {
                // Collecte namespaces
                PsiTreeUtil.collectElementsOfType(element, DSMNamespaceDeclaration::class.java)
                    .map { DSMStructureViewElement(it) }  // ← PSI utilisé
                    .toTypedArray()
            }
            is DSMNamespaceDeclaration -> {
                // Collecte concepts, structs dans ce namespace
                val children = mutableListOf<DSMStructureViewElement>()

                PsiTreeUtil.collectElementsOfType(element, DSMConceptDeclaration::class.java)
                    .forEach { children.add(DSMStructureViewElement(it)) }  // ← PSI utilisé

                PsiTreeUtil.collectElementsOfType(element, DSMStructDeclaration::class.java)
                    .forEach { children.add(DSMStructureViewElement(it)) }  // ← PSI utilisé

                children.toTypedArray()
            }
            else -> emptyArray()
        }
    }

    override fun getPresentation(): ItemPresentation {
        return when (element) {
            is DSMNamedElement -> {  // ← Interface commune (concept, struct, enum)
                PresentationData(
                    element.name,           // ← PSI API getName()
                    null,
                    element.getIcon(0),     // ← PSI API getIcon()
                    null
                )
            }
            else -> ...
        }
    }
}
```

**PSI utilisé:**
- DSMFile, DSMNamespaceDeclaration (générés)
- DSMConceptDeclaration, DSMStructDeclaration (générés)
- DSMNamedElement.getName() (mixin)

**Résultat:** Structure panel montre hiérarchie namespace → concepts/structs.

#### Catégorie 6: Services & UI (5 extension points)

**Responsabilité:** Settings, validation, tool windows

| Extension Point | Responsabilité | PSI utilisé |
|-----------------|----------------|-------------|
| `applicationConfigurable` | Settings page | N/A |
| `applicationService` | Settings storage | N/A |
| `projectService` (×2) | Validation services | DSMFile |
| `toolWindow` | Validation panel UI | N/A (UI) |

**Ces extension points utilisent moins les PSI** (plutôt des services/UI).

### Le Pattern Commun

**Tous les extension points suivent le même pattern:**

```
1. Platform détecte un événement (hover, click, open file, etc.)
    ↓
2. Platform cherche extension point enregistré pour language="DSM"
    ↓
3. Platform instancie ton implémentation
    ↓
4. Platform passe le PSI element approprié
    ↓
5. Ton code utilise l'API PSI (générée par Grammar-Kit)
    ↓
6. Ton code retourne résultat
    ↓
7. Platform affiche dans l'IDE
```

**Les PSI générés sont le cœur** - sans eux, les extension points n'auraient rien à consommer.

### Résumé Niveau 4

✅ **21 extension points = La machinerie:** Chacun a une responsabilité précise
✅ **Tous consomment les PSI générés:** Interface commune (DSMConceptDeclaration, etc.)
✅ **Pattern uniforme:** Platform → Extension point → PSI API → Résultat
✅ **Grammar-Kit fournit les pièces:** Extension points les assemblent en features

**Prochaine étape:** Vue d'ensemble du système complet.

---

## Niveau 5: Le Système Complet

### Vue d'ensemble: De la Grammaire à l'IDE

**Le flux complet en 7 étapes:**

```
┌─────────────────────────────────────────────────────────────┐
│ Niveau 0: FONDATION                                         │
│ DSM.bnf (400 lignes)                                        │
│   - Grammaire syntaxique                                    │
│   - Métadonnées IntelliJ (mixin, implements, etc.)          │
└─────────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────────┐
│ Niveau 1: TRANSFORMATION                                    │
│ ./gradlew generateParser                                    │
│   Grammar-Kit lit DSM.bnf                                   │
│   Génère ~5000 lignes de code Java                          │
└─────────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────────┐
│ Niveau 2: CODE GÉNÉRÉ (Les Pièces)                          │
│ - Parser (DSMParser.java)                                   │
│ - Interfaces PSI (DSMConceptDeclaration.java, etc.)         │
│ - Implémentations PSI (DSMConceptDeclarationImpl.java)      │
│ - Types (DSMTypes.java)                                     │
│ - Visitors (DSMVisitor.java)                                │
└─────────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────────┐
│ Niveau 3: RUNTIME (L'Exécution)                             │
│ User ouvre fichier.dsm                                      │
│   ↓                                                          │
│ Parser → AST (toujours créé)                                │
│   ↓                                                          │
│ User navigue → PSI wrapper créé (lazy)                      │
└─────────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────────┐
│ Niveau 4: MACHINERIE (Extension Points - 21)                │
│                                                              │
│ Syntax Highlighting → Annotator                             │
│   - Consomme: DSMUserTypeReference                          │
│   - Produit: Code coloré                                    │
│                                                              │
│ Documentation → DocumentationProvider                        │
│   - Consomme: DSMConceptDeclaration.getName(), .docString   │
│   - Produit: Popup documentation                            │
│                                                              │
│ Symbol Search → ChooseByNameContributor                     │
│   - Consomme: DSMConceptDeclaration.getName()               │
│   - Produit: Liste de symboles                              │
│                                                              │
│ Structure View → PsiStructureViewFactory                    │
│   - Consomme: DSMNamespaceDeclaration, DSMConceptDeclaration│
│   - Produit: Arbre de structure                             │
│                                                              │
│ ... 17 autres extension points ...                          │
└─────────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────────┐
│ RÉSULTAT: Plugin IDE Complet                                │
│ - Syntax highlighting ✓                                     │
│ - Code completion ✓                                         │
│ - Navigation (Ctrl+Click) ✓                                 │
│ - Documentation (hover) ✓                                   │
│ - Symbol search (Cmd+O) ✓                                   │
│ - Structure view (Cmd+7) ✓                                  │
│ - Find usages (Alt+F7) ✓                                    │
│ - Code formatting (Cmd+Alt+L) ✓                             │
│ - ... et 13 autres features ...                             │
└─────────────────────────────────────────────────────────────┘
```

### Le Rôle de Chaque Niveau

| Niveau | Rôle | Input | Output | Qui fait | Quand |
|--------|------|-------|--------|----------|-------|
| **0. Fondation** | Déclarer | Idées | DSM.bnf | Toi | Design time |
| **1. Transformation** | Générer | DSM.bnf | Code Java | Grammar-Kit | Build time |
| **2. Pièces** | Structurer | Code généré | Classes compilées | Gradle | Compile time |
| **3. Runtime** | Exécuter | Fichier .dsm | AST + PSI | Platform | Runtime |
| **4. Machinerie** | Fournir features | PSI | Features IDE | Extension points | Runtime |

### Exemple Complet: De BNF à Feature

**Scénario:** User veut documentation sur hover

**Niveau 0: Tu écris dans DSM.bnf**
```bnf
conceptDeclaration ::=
    DOC_STRING?  // ← Clé pour documentation
    CONCEPT IDENTIFIER conceptInheritance? SEMICOLON
    {
        mixin="...DSMNamedElementImpl"
        implements="...DSMNamedElement"
        methods=[getName ...]
    }
```

**Niveau 1: Grammar-Kit génère**
```java
// Interface
public interface DSMConceptDeclaration extends DSMNamedElement {
    @Nullable PsiElement getDocString();  // ← Méthode générée pour DOC_STRING?
    @Nullable PsiElement getIdentifier();
    String getName();  // ← Méthode de l'attribut methods
}

// Implémentation
public class DSMConceptDeclarationImpl extends DSMNamedElementImpl
                                       implements DSMConceptDeclaration {
    public PsiElement getDocString() {
        return findChildByType(DOC_STRING);  // ← Cherche dans AST
    }
}
```

**Niveau 2: Code compilé**
- DSMConceptDeclaration.class
- DSMConceptDeclarationImpl.class
- DSMParser.class

**Niveau 3: Runtime - User ouvre fichier**
```dsm
"""A vehicle in the simulation"""
concept Vehicle;
```

```
Parser:
    Reconnaît "concept Vehicle;"
    Construit AST:
        ASTNode(CONCEPT_DECLARATION)
          ├─ ASTNode(DOC_STRING, "\"\"\"A vehicle...\"\"\"")
          ├─ ASTNode(CONCEPT)
          ├─ ASTNode(IDENTIFIER, "Vehicle")
          └─ ASTNode(SEMICOLON)
```

**Niveau 4: User hover sur "Vehicle"**
```
Platform:
    Événement hover à offset 30
        ↓
    Cherche extension point lang.documentationProvider pour DSM
        ↓
    Trouve: DSMDocumentationProvider
        ↓
    Obtient PSI element:
        file.findElementAt(30) → DSMConceptDeclaration wrapper
        Wraps: ASTNode(CONCEPT_DECLARATION)
        ↓
    Appelle: provider.generateDoc(element, null)
        ↓
DSMDocumentationProvider:
    element is DSMConceptDeclaration → true
    name = element.getName()           // "Vehicle" (via mixin)
    doc = element.getDocString()?.text // "\"\"\"A vehicle...\"\"\"" (généré)

    return "<html><b>Concept: Vehicle</b><p>A vehicle in the simulation</p></html>"
        ↓
Platform:
    Affiche popup documentation
```

**Résultat visible:** Popup avec "**Concept: Vehicle** A vehicle in the simulation"

**Traçabilité complète:**
```
DSM.bnf ligne 117: DOC_STRING?
    ↓
Grammar-Kit génère: getDocString()
    ↓
Runtime: element.getDocString()?.text
    ↓
Documentation affichée
```

**Sans DOC_STRING? dans la BNF → Pas de getDocString() → Pas de documentation possible!**

### Architecture en Couches - Schéma Final

```
┌─────────────────────────────────────────────────────────────┐
│ USER                                                         │
│ Voit: Syntax highlighting, documentation, navigation        │
└─────────────────────────────────────────────────────────────┘
                        ↑ Features
┌─────────────────────────────────────────────────────────────┐
│ EXTENSION POINTS (Machinerie - 21)                          │
│ Annotator, DocumentationProvider, ChooseByNameContributor   │
└─────────────────────────────────────────────────────────────┘
                        ↑ Consomme
┌─────────────────────────────────────────────────────────────┐
│ TON CODE MÉTIER                                             │
│ DSMDocumentationProvider, DSMSemanticHighlightingAnnotator  │
└─────────────────────────────────────────────────────────────┘
                        ↑ Utilise
┌─────────────────────────────────────────────────────────────┐
│ PSI API (Interfaces générées)                               │
│ DSMConceptDeclaration, DSMStructDeclaration                 │
│ Méthodes: getName(), getDocString(), getIdentifier()        │
└─────────────────────────────────────────────────────────────┘
                        ↑ Implémenté par
┌─────────────────────────────────────────────────────────────┐
│ PSI IMPLÉMENTATIONS (Classes générées)                      │
│ DSMConceptDeclarationImpl, DSMStructDeclarationImpl         │
│ Délègue vers AST                                            │
└─────────────────────────────────────────────────────────────┘
                        ↑ Wraps
┌─────────────────────────────────────────────────────────────┐
│ AST (Abstract Syntax Tree)                                  │
│ ASTNode(CONCEPT_DECLARATION), ASTNode(IDENTIFIER)           │
│ Stockage immuable                                           │
└─────────────────────────────────────────────────────────────┘
                        ↑ Construit par
┌─────────────────────────────────────────────────────────────┐
│ PARSER (Code généré)                                        │
│ DSMParser.conceptDeclaration(), DSMParser.structDeclaration()│
└─────────────────────────────────────────────────────────────┘
                        ↑ Généré par
┌─────────────────────────────────────────────────────────────┐
│ GRAMMAR-KIT (Transformateur)                                │
│ Lit DSM.bnf → Génère code                                   │
└─────────────────────────────────────────────────────────────┘
                        ↑ Lit
┌─────────────────────────────────────────────────────────────┐
│ DSM.BNF (Fondation)                                          │
│ Grammaire + Métadonnées IntelliJ Platform                   │
│ SOURCE DE VÉRITÉ CENTRALE                                   │
└─────────────────────────────────────────────────────────────┘
```

### Les Principes Fondamentaux

**1. BNF-Driven Architecture**
- TOUT part de DSM.bnf
- Grammaire = source de vérité
- Métadonnées = instructions de génération
- Modifications: Change BNF → Régénère → Architecture complète mise à jour

**2. Progressive Disclosure**
- Niveau 0: Simple (grammaire déclarative)
- Niveau 1-4: Complexité croissante
- Chaque niveau construit sur le précédent
- Bottom-up: Fondation solide → Système complet

**3. Separation of Concerns**
- Parser: Reconnaissance syntaxique
- AST: Stockage structure
- PSI: API sémantique
- Extension points: Features IDE
- Chaque couche a une responsabilité claire

**4. Code Generation**
- Pas de boilerplate manuel
- Architecture garantie cohérente
- Modifications faciles (change BNF)
- ~5000 lignes générées depuis 400 lignes BNF

**5. Extension Points as Machinery**
- 21 extension points = 21 features
- Chacun consomme PSI
- Pattern uniforme
- Intégration Platform transparente

### Résumé Final

**Question initiale:** Comment un fichier `.bnf` devient un plugin IDE complet?

**Réponse:**

✅ **Niveau 0:** Tu écris DSM.bnf (grammaire + métadonnées) - **La fondation**
✅ **Niveau 1:** Grammar-Kit transforme BNF en code (parser + PSI) - **Le transformateur**
✅ **Niveau 2:** Code généré fournit les pièces (interfaces, impls, visitors) - **Les pièces**
✅ **Niveau 3:** Runtime exécute (parse → AST → PSI lazy) - **L'exécution**
✅ **Niveau 4:** Extension points consomment PSI (21 features) - **La machinerie**

**Résultat:** Plugin IDE complet avec syntax highlighting, navigation, completion, documentation, etc.

**La clé:** BNF étendu = DSL déclaratif qui génère une architecture complète intégrée à IntelliJ Platform.

**Architecture BNF-Driven:** Simple à écrire, puissante à l'exécution, cohérente par construction.

---

## Conclusion: La Puissance de l'Approche Déclarative

### Ce que tu as appris

**Avant:**
- ❓ Architecture IntelliJ semble magique
- ❓ Classes PSI apparaissent de nulle part
- ❓ Extension points semblent complexes
- ❓ Difficile de comprendre comment tout s'articule

**Après:**
- ✅ TOUT part de DSM.bnf (source de vérité)
- ✅ Grammar-Kit = compilateur d'architecture
- ✅ Code généré = pièces cohérentes
- ✅ Extension points = machinerie qui consomme PSI
- ✅ Architecture prévisible et modifiable

### Les Avantages de l'Architecture BNF-Driven

**1. Déclaratif vs Impératif**
```
Traditionnel (impératif):
- Écrire parser manuellement
- Écrire classes AST manuellement
- Écrire visitors manuellement
- Écrire glue code manuellement
→ Des milliers de lignes, bugs, incohérences

IntelliJ/Grammar-Kit (déclaratif):
- Écrire DSM.bnf (400 lignes)
- Régénérer
→ 5000 lignes générées, cohérentes, sans bugs
```

**2. Single Source of Truth**
- DSM.bnf = unique source
- Modifications centralisées
- Cohérence garantie
- Documentation intrinsèque

**3. Évolution Facile**
```
Besoin: Ajouter UUID aux concepts

Traditionnel:
- Modifier parser (manuel)
- Modifier classe AST (manuel)
- Modifier classe PSI (manuel)
- Modifier visitor (manuel)
- Modifier tous les usages (manuel)
→ Risque d'oublis, bugs

BNF-Driven:
- Ajouter UUID dans DSM.bnf (1 ligne)
- Régénérer
→ Tout mis à jour automatiquement
```

**4. Architecture Prouvée**
- Pattern IntelliJ Platform standard
- Utilisé par tous les plugins JetBrains
- Des milliers de plugins tiers
- Robuste, testé, documenté

### Pour Aller Plus Loin

**Maintenant que tu comprends l'architecture:**

1. **Modifier la grammaire:**
   - Change DSM.bnf
   - Régénère: `./gradlew generateParser`
   - Observe le code généré

2. **Ajouter un extension point:**
   - Regarde `.claude/platform-architecture-guide.md`
   - Choisis un extension point
   - Implémente en utilisant l'API PSI générée

3. **Fixer la dette technique:**
   - P0: DSMDocumentationProvider
   - Utilise `element.getDocString()?.text` (PSI API)
   - Au lieu de regex text scanning

4. **Explorer les plugins de référence:**
   - Rust plugin: github.com/intellij-rust/intellij-rust
   - TOML plugin: dans le repo Rust
   - Voir comment ils utilisent Grammar-Kit

### Ressources

**Guides créés:**
- `.claude/java-for-intellij-platform-primer.md` - Java pour Platform SDK
- `.claude/platform-architecture-guide.md` - Extension points catalog
- `.claude/grammar-kit-guide.md` - BNF → PSI generation
- `.claude/ast-psi-responsibilities.md` - Responsabilités des couches

**Documentation officielle:**
- Grammar-Kit: github.com/JetBrains/Grammar-Kit
- IntelliJ Platform SDK: plugins.jetbrains.com/docs/intellij/

---

**Fin du Guide**

**Version:** 1.0
**Date:** 2025-11-19
**Auteur:** Digital Substrate + Claude Code

**Tu es maintenant prêt à:**
- ✅ Comprendre toute l'architecture
- ✅ Modifier le plugin en confiance
- ✅ Ajouter de nouvelles features
- ✅ Fixer la dette technique
- ✅ Expliquer l'architecture à d'autres

**Bravo! 🎉**
