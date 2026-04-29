# Changelog

All notable changes to the DSM Language Support plugin will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.2.9] - 2026-04-29

### Fixed
- **Validate button (tool window)**: now actually triggers validation. The previous implementation went through the action system from a Swing listener and was silently disabled when `e.project` could not be resolved from the button's data context.
- **Validation target**: Validate button used to always pass `project.basePath` to `dsm_util.py check`, which does not recurse into independent sub-projects. Now resolves to the directory of the current Project view selection, falling back to the open editor file's directory, then to `project.basePath`. Always sends a directory (sibling files are required to resolve cross-references).
- **Crash reporting**: when `dsm_util.py` exits non-zero with no parsable diagnostics (e.g. Python `ModuleNotFoundError`), the tool window used to silently report `✓ No errors found`. It now switches to a multi-line text view showing the raw stderr/stdout, with a red badge and error count in the tab title.
- **EDT threading**: `FileDocumentManager.saveAllDocuments()` is now wrapped in `WriteIntentReadAction.run`, fixing the `Access is allowed from write thread only` exception introduced by the 2025.1 threading model.

### Changed
- Removed the two deprecated API usages flagged by JetBrains Marketplace:
  - `TemplateContextType(String, String)` → single-arg constructor (`contextId` already provided via `<liveTemplateContext>` in `plugin.xml`)
  - `ActionUtil.invokeAction(action, dataContext, …)` removed in favor of a direct service call from the tool window panel

## [1.2.7] - 2026-04-09

### Changed
- Target IntelliJ 2025.1+ (was 2024.3)
- Kotlin 2.1.0 (was 1.9.25)
- Replace deprecated `FilenameIndex.getAllFilesByExt` with `FileTypeIndex.getFiles`
- Replace internal `IElementType.debugName` with private field
- Replace `AnAction.actionPerformed()` (override-only) with `ActionUtil`
- Published on [JetBrains Marketplace](https://plugins.jetbrains.com)

### Known Issues
- 2 deprecated constructors (`TemplateContextType`, `AnActionEvent`) — no replacement available yet, suppressed

## [1.2.3] - 2025-11-25

### Added
- **Context-Aware Code Completion**: Smart completion for type parameters
  - Inside `key<T>`, suggests only concepts and clubs (not primitives or structs)
  - Inside `attachment<T, D>`, first parameter (T) suggests concepts/clubs, second parameter (D) accepts any type
  - Includes `any_concept` keyword
  - Shows file location for each suggestion
  - Implemented via `ConceptCompletionProvider`

### Changed
- **Documentation Provider**: Refactored to use PSI-based extraction
  - Now uses `DSMPsiUtil.getDocString()` for proper AST navigation
  - Follows IntelliJ Platform best practices (no regex text scanning)
  - Quality upgraded from ⭐⭐ to ⭐⭐⭐

### Fixed
- **Code Completion**: Removed duplicate generic types (now handled by Live Templates)
- Documentation audit and synchronization

## [1.2.2] - 2025-11-24

### Added
- **File Template**: Create new DSM files from template
  - Available via New → DSM File in context menu and File menu
  - Automatically generates namespace with capitalized name
  - Generates UUID for new files
  - Creates properly formatted DSM file structure

## [1.2.1] - 2025-11-18

### Added
- **Symbol Search**: Quick navigation to DSM types by name
  - Navigate with `Alt+Cmd+O` (macOS) / `Ctrl+Alt+N` (Windows/Linux)
  - Find concepts, structs, enums, clubs, and function pools across all project files
  - Type-specific icons: concepts (class/blue C), structs (static/orange S), enums (purple E), clubs (interface), function pools (function)
  - Shows file location for each result
  - Direct navigation to definition on selection
  - File-based scanning implementation (no stub indexes required)
- **Documentation on Hover**: Quick documentation display for DSM elements
  - View documentation with `F1` (macOS) / `Ctrl+Q` (Windows/Linux)
  - Shows triple-quoted docstrings (`"""..."""`) for all DSM elements
  - Supports concepts, structs, enums, clubs, attachments, function pools, fields, functions, enum values
  - Formatted popup with definition header, content section, and metadata (type, file location)
  - Rich formatting with bold element names and inheritance information
  - Implementation note: Uses text-based extraction (architectural refactoring planned for future versions)
- **Live Templates**: Comprehensive set of code snippets for rapid development
  - Essential DSM constructs: `struct`, `concept`, `enum`, `namespace`, `attachment`, `function_pool`
  - Type shortcuts: `vec`, `key`, `optional`, `map`, `set`, `vector`, `tuple`, `variant`
  - Advanced patterns: `isa`, `club`, `docstr`, `keyfield`, `mutable`, `query`
  - Tab to expand with automatic cursor positioning
  - Ported from VS Code extension
- **Rename Refactoring**: Safe cross-file renaming with preview
  - Rename types with `Shift+F6`
  - Updates all references across project
  - Preview changes before applying
- **Code Folding**: Collapse and expand code blocks
  - Fold namespaces, concepts, clubs, structs, enums, function pools
  - Custom placeholder text for each element type
  - Expanded by default for better readability
- **Code Commenting**: Line and block comment support
  - Toggle line comments with `Cmd+/` / `Ctrl+/`
  - Block comments with `Cmd+Opt+/` / `Ctrl+Shift+/`
- **Semantic Highlighting**: Type-based coloring with formatting
  - Definitions in **bold** (concept, struct, enum, club, function pool)
  - Fields in *italic* (mutable fields distinguished)
  - Recursive highlighting in generic types
  - User-configurable colors in Settings

### Changed
- **DSM Validation**: Refactored into `DSMValidationService` for better maintainability
- **Code Completion**: Simplified to avoid conflicts with Live Templates

## [1.2.0] - 2025-11-17

### Added
- **Code Formatting**: Complete formatting support with `Cmd+Alt+L` / `Ctrl+Alt+L`
  - Smart indentation for struct and enum fields (4 spaces default)
  - No indentation for namespace, function_pool, and attachment_function_pool bodies
  - Automatic spacing around operators, commas, and type parameters
  - List literals stay on a single line (e.g., `position = {0.0, 0.0, 0.0};`)
  - Semicolons always remain on the same line as closing braces
  - Configurable indent size, tab size, and spacing preferences in Code Style settings
- **Brace Matching**: Highlight and navigate between matching delimiters
  - Automatic highlighting of matching `{}`, `[]`, `()`, `<>` pairs
  - Jump to matching brace with `Cmd+Shift+M` / `Ctrl+Shift+M`
  - Expand selection to enclosing block with `Cmd+W` / `Ctrl+W`
- **Plugin Icon**: Added Digital Substrate logo as plugin icon

### Changed
- Enhanced plugin description with comprehensive feature documentation
- Improved README with detailed formatting rules and brace matching instructions

### Fixed
- Corrected spacing rules for type parameters and attachments
- Fixed line break behavior for list literals and semicolons

## [1.0.0] - 2025-01-11

### Added
- **Language Support**: Complete DSM language definition and file type registration
- **Lexer & Parser**: Full lexical and syntactic analysis using JFlex and Grammar-Kit
- **Syntax Highlighting**: Comprehensive color scheme for keywords, types, literals, and comments
  - Keywords: `namespace`, `concept`, `struct`, `enum`, `club`, `membership`, `attachment`, `function_pool`, `attachment_function_pool`
  - Primitive types: `bool`, `int8-64`, `uint8-64`, `float`, `double`, `string`, `uuid`, `blob_id`, `commit_id`
  - Generic types: `key`, `optional`, `vector`, `set`, `map`, `tuple`, `variant`, `xarray`, `vec`, `mat`
  - Special elements: UUIDs, enum values (`.EnumValue`), doc strings (`"""..."""`)
  - Semantic coloring: Distinct colors for concepts, structs, enums, and function pools
- **Code Completion**: Context-aware completions for keywords, types, and identifiers
  - All DSM keywords and type suggestions
  - Smart positioning based on current context
- **Navigation & References**: Advanced code navigation features
  - Go to Definition: Navigate to concept, struct, enum, or type definitions (`Cmd+B` / `Ctrl+B`)
  - Find Usages: Find all references to a symbol (`Opt+F7` / `Alt+F7`)
  - Type Hierarchy: Explore concept inheritance hierarchies (`Ctrl+H`)
  - Quick Documentation: View documentation for symbols (`F1` / `Ctrl+Q`)
  - Intra-file and inter-file reference resolution
- **Structure View**: Hierarchical tree view of DSM file structure
  - Namespace, concepts, structs, enums, attachments, and function pools
  - Namespace merging for declarations with the same UUID
  - Quick navigation by clicking elements (`Cmd+7` / `Alt+7`)
- **DSM Validation**: Integration with `dsm_util.py` for manual validation
  - Manual validation triggered by user action (`Ctrl+Alt+V`)
  - Dedicated DSM Validation tool window with error table
  - Error highlighting with red wavy underlines in editor
  - Error details on hover
  - Quick navigation: Double-click errors to jump to location
  - Keyboard shortcuts: `Alt+F2` for next error, `Alt+Shift+F2` for previous error
  - Error navigation toolbar with Previous/Next buttons
  - Configurable paths for Python virtual environment and `dsm_util.py`
  - Configurable validation timeout
- **Settings UI**: Configuration page for DSM validation settings
  - Python Virtual Environment path configuration
  - `dsm_util.py` path configuration
  - Validation timeout setting

### Technical
- Built with IntelliJ Platform SDK 2024.3
- Kotlin 1.9.25
- JVM 21 target
- Gradle 8.5
- Grammar-Kit 2022.3.2.2 for parser generation
- JFlex for lexer generation

---

## Release Notes

### Version 1.2.0
This release adds powerful code formatting and navigation features to enhance the DSM editing experience. The formatter provides intelligent indentation that respects DSM's structure, keeping namespace and function pool bodies flat while properly indenting struct and enum fields. Brace matching enables quick navigation between matching delimiters and smart block selection.

### Version 1.0.0
Initial release of the DSM Language Support plugin for JetBrains IDEs. Provides complete language support including syntax highlighting, code completion, navigation, structure view, and integration with DSM validation tools. This release establishes the foundation for professional DSM development in IntelliJ IDEA, CLion, PyCharm, and all JetBrains IDEs.

---

**Digital Substrate** | [Documentation](../doc/DSM.md) | [Getting Started](../doc/Getting_Started.md) | [PRP](PRP.md)
