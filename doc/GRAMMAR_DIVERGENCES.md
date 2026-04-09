# Grammar Divergences: DSM.g4 vs DSM.bnf

**Version:** 1.0
**Date:** 2025-11-20
**Status:** Documented & Accepted

---

## Overview

This document describes intentional divergences between the reference grammar (`dsm/DSM.g4` - ANTLR4) and the JetBrains plugin grammar (`src/main/kotlin/com/digitalsubstrate/dsm/parser/DSM.bnf` - Grammar-Kit).

---

## Divergence #1: Namespace Docstring

### DSM.g4 (Reference Parser - C++)

```antlr
namespace: 'namespace' IDENTIFIER UUID '{' namespaceDefinitions '}' ';' ;
```

**Behavior:** Namespace declarations **CANNOT** have a DOCSTR prefix. The parser will reject:

```dsm
"""This will cause a parsing error"""
namespace Graph {27c49329-a399-415c-baf0-db42949d2ba2} {
    concept Vertex;
}
```

### DSM.bnf (JetBrains Plugin Parser)

```bnf
namespaceDeclaration ::=
    DOC_STRING?
    NAMESPACE IDENTIFIER UUID LBRACE namespaceBody RBRACE SEMICOLON
```

**Behavior:** Namespace declarations **CAN** have a DOC_STRING prefix (treated as comment, ignored). The plugin will accept:

```dsm
"""This will be ignored (treated as comment)"""
namespace Graph {27c49329-a399-415c-baf0-db42949d2ba2} {
    concept Vertex;
}
```

---

## Architectural Justification

### Why DSM.g4 Forbids Namespace Docstrings

**Namespaces are open declarations:**

```dsm
// File A
"""Documentation version A"""
namespace Graph {27c49329-a399-415c-baf0-db42949d2ba2} {
    concept Vertex;
}

// File B
"""Documentation version B"""
namespace Graph {27c49329-a399-415c-baf0-db42949d2ba2} {
    concept Edge;
}
```

**Problem during concatenation:**
- When parsing multiple files, namespaces are merged by UUID
- Which docstring should be kept? A? B? Both? In what order?
- No clear semantic merge strategy

**Solution:** Namespace declarations cannot have docstrings (architectural decision).

### Why DSM.bnf Permits Namespace Docstrings

**Technical constraint:** Grammar-Kit uses PEG (Parsing Expression Grammar) instead of ANTLR4's LL(\*).

**PEG limitation:** When the parser sees a `DOC_STRING`, it cannot distinguish which rule to apply:
- `conceptDeclaration` (starts with `DOC_STRING? CONCEPT`)
- `structDeclaration` (starts with `DOC_STRING? STRUCT`)
- `namespaceDeclaration` (starts with `NAMESPACE`)

**Attempted solution:** Factorizing `DOC_STRING?` at higher levels (e.g., `documentedNamespaceElement`) created complex parser rules that still failed due to PEG's ordered choice semantics.

**Pragmatic solution:** Treat `DOC_STRING` as a comment token (filtered before parsing). This:
- ✅ Allows the plugin to work correctly
- ✅ Follows IntelliJ Platform conventions (docstrings = structured comments)
- ✅ Permits namespace docstrings (ignored, no semantic impact)
- ⚠️ Creates divergence with reference parser

---

## Implications

### For Plugin Users

**During development (JetBrains plugin):**
- Namespace docstrings are **permitted** but **ignored**
- No parsing errors
- Permissive editing experience

**Before commit (reference parser):**
- Use `python3 tools/dsm_util.py check <file.dsm>` for strict validation
- Namespace docstrings will **cause errors**
- Ensures files conform to canonical DSM grammar

### Workflow Recommendation

```bash
# During development - use plugin (permissive)
# Edit DSM files in IDE, docstrings on namespaces OK

# Before commit - validate with reference parser (strict)
python3 tools/dsm_util.py check my_file.dsm

# If errors about namespace docstrings:
# Remove them before committing
```

---

## Technical Details

### DSMParserDefinition.kt

```kotlin
val COMMENTS = TokenSet.create(
    DSMTypes.LINE_COMMENT,
    DSMTypes.DOC_STRING  // Treated as structured comment
)
```

**Effect:** `DOC_STRING` tokens are filtered out before the parser sees them. The parser never needs to handle `DOC_STRING` explicitly - it's invisible during parsing, extracted later via PSI traversal.

### Alternative Approaches Considered

1. **Factorize DOC_STRING at higher levels**
   ❌ PEG ordered choice semantics caused ambiguity
   ❌ Complex grammar rules still failed parsing

2. **Modify DSM.g4 to allow namespace docstrings**
   ❌ Architectural problem: no clear merge strategy for open namespaces
   ❌ Would affect reference parser behavior

3. **Current approach: DOC_STRING as comment**
   ✅ Simple, works with PEG constraints
   ✅ Standard IntelliJ Platform pattern
   ✅ Validation available via `dsm_util.py check`

---

## Parser Technology Comparison

### ANTLR4 (DSM.g4) - LL(\*)
- **Lookahead:** Unlimited (builds DFA for disambiguation)
- **Backtracking:** Automatic (can undo token consumption)
- **Ambiguity resolution:** Analyzes grammar, chooses best path

**Example:**
```antlr
concept: DOCSTR? 'concept' ...
struct: DOCSTR? 'struct' ...
```

When ANTLR4 sees `DOCSTR`, it looks ahead to the next token (`concept` or `struct`) and chooses the correct rule **before consuming DOCSTR**.

### Grammar-Kit (DSM.bnf) - PEG
- **Lookahead:** Limited (fixed k-token)
- **Backtracking:** None (once token consumed, cannot undo)
- **Ambiguity resolution:** Ordered choice (first match wins)

**Example:**
```bnf
private namespaceElement ::=
    conceptDeclaration  // DOC_STRING? CONCEPT ...
    | structDeclaration // DOC_STRING? STRUCT ...
```

When PEG sees `DOC_STRING`, it tries `conceptDeclaration`:
- Consumes `DOC_STRING` (optional, always succeeds)
- Looks for `CONCEPT` → not found → **fails completely**
- Cannot backtrack to try `structDeclaration`

**Solution:** Remove `DOC_STRING` from parser visibility (treat as comment).

---

## Conclusion

The divergence between DSM.g4 and DSM.bnf regarding namespace docstrings is:

1. **Architecturally justified** (namespace open declaration semantics)
2. **Technically necessary** (PEG parser limitations)
3. **Pragmatically acceptable** (validation available via `dsm_util.py`)

Users should rely on `dsm_util.py check` for strict validation before committing DSM files.

---

**Digital Substrate** | Grammar-Kit PEG vs ANTLR4 LL(\*) | Pragmatism > Purity
