# DSM Language Support for JetBrains IDEs

**Official JetBrains plugin for the Digital Substrate Model (DSM) language**

The DSM plugin provides comprehensive language support for `.dsm` files in all JetBrains IDEs, including syntax highlighting, intelligent code completion, advanced navigation, structure view, and validation via `dsm_util.py`.

## Features

### Syntax Highlighting
- **Keywords**: `namespace`, `concept`, `club`, `membership`, `enum`, `struct`, `attachment`, `function_pool`, `attachment_function_pool`
- **Type System**:
  - Primitives: `bool`, `int8-64`, `uint8-64`, `float`, `double`, `string`, `uuid`, `blob_id`
  - Collections: `vec`, `mat`, `tuple`, `optional`, `vector`, `set`, `map`, `xarray`, `variant`
  - References: `key<T>`
- **Special Elements**: UUIDs, enum values (`.EnumValue`), doc strings (`"""..."""`)
- **Semantic Coloring**: Concepts, structs, enums, attachments, and function pools each have distinct colors

### Code Completion
- **Context-Aware**: Completions adapt to the current context
  - Inside `key<T>`, suggests only concepts and clubs (not primitives or structs)
  - Inside `attachment<T, D>`, first parameter (T) suggests concepts/clubs, second parameter (D) accepts any type
  - Includes `any_concept` keyword in type parameter contexts
  - Shows file location for each suggestion
- **Keyword Completion**: `is a`, `true`, `false`
- **Type Completion**: All primitive types (bool, int8-64, uint8-64, float, double, string, uuid, blob_id, commit_id, any, any_concept)

### Navigation
- **Go to Definition**: Navigate to concept, struct, enum, or type definitions
  - `Cmd+B` (macOS) / `Ctrl+B` (Windows/Linux)
  - `Cmd+Click` (macOS) / `Ctrl+Click` (Windows/Linux)
- **Find Usages**: Find all references to a symbol
  - `Opt+F7` (macOS) / `Alt+F7` (Windows/Linux)
- **Type Hierarchy**: Explore concept inheritance hierarchies
  - `Ctrl+H` (macOS/Windows/Linux)
- **Symbol Search**: Quick navigation to DSM types by name
  - `Alt+Cmd+O` (macOS) / `Ctrl+Alt+N` (Windows/Linux)
  - Find concepts, structs, enums, clubs, and function pools across all project files
  - Type-specific icons differentiate each element type
- **Quick Documentation**: View documentation from triple-quoted docstrings
  - `F1` (macOS) / `Ctrl+Q` (Windows/Linux)
  - Displays docstrings (`"""..."""`) with rich formatting
  - Shows type information, inheritance, and file location

### Structure View
- **Hierarchical Tree**: Navigate your DSM file structure at a glance
  - `Cmd+7` (macOS) / `Alt+7` (Windows/Linux)
- **Smart Grouping**: Namespaces, concepts, structs, enums, attachments, and function pools
- **Namespace Merging**: Multiple namespace declarations with the same UUID are merged
- **Quick Navigation**: Click any element to jump to its definition

### DSM Validation
- **Manual Validation**: Explicit validation triggered by user action (`Ctrl+Alt+V`)
- **Validation Tool Window**: Dedicated panel showing all validation errors with file/line/column details
- **Error Highlighting**: Errors highlighted in editor with red wavy underlines
- **Error Details**: Hover over underlines to see detailed messages
- **Quick Navigation**:
  - Double-click errors in tool window to jump to location
  - Use "Previous Error" (`Alt+Shift+F2`) and "Next Error" (`Alt+F2`) buttons in toolbar
  - Keyboard shortcuts: `Alt+F2` for next error, `Alt+Shift+F2` for previous error

### Editor Features
- **Auto-Closing**: Automatic closing of brackets `{}`, `[]`, `()`, `<>` and quotes
- **Smart Indentation**: Automatic indentation after declarations and inside code blocks
- **Code Formatting**: Reformat code with `Cmd+Alt+L` (macOS) / `Ctrl+Alt+L` (Windows/Linux)
  - Configurable indentation size (default: 4 spaces)
  - No indentation for namespace, function_pool, and attachment_function_pool bodies
  - Proper indentation for struct and enum fields only
  - Automatic spacing around operators, braces, and commas
  - List literals stay on a single line (e.g., `{0.0, 0.0, 0.0}`)
  - Semicolons always stay on the same line
- **Brace Matching**: Highlight and navigate between matching `{}`, `[]`, `()`, `<>` pairs
  - Automatic highlighting when cursor is on a brace
  - Jump to matching brace with `Cmd+Shift+M` (macOS) / `Ctrl+Shift+M` (Windows/Linux)
  - Expand selection to enclosing block with `Cmd+W` (macOS) / `Ctrl+W` (Windows/Linux)
- **Code Folding**: Collapse/expand namespaces, structs, enums, and other blocks
- **Comment Handling**: Line comments with `//`

## Installation

### Prerequisites
- **JetBrains IDE**: IntelliJ IDEA 2024.3+, CLion 2024.3+, PyCharm 2024.3+, or any JetBrains IDE 2024.3+
- **Platform Build**: 243 or higher (check in `Help` → `About`)

### Install Plugin from Disk

1. **Download** the plugin: `dsm-jetbrains-plugin-1.2.3.zip`

2. **Open Settings/Preferences**:
   - macOS: `IntelliJ IDEA` → `Preferences` (or `Cmd+,`)
   - Windows/Linux: `File` → `Settings` (or `Ctrl+Alt+S`)

3. **Navigate to Plugins**:
   - Click `Plugins` in the left sidebar

4. **Install from Disk**:
   - Click the gear icon (⚙️) → `Install Plugin from Disk...`
   - Select the downloaded `dsm-jetbrains-plugin-1.2.3.zip`
   - Click `OK`

5. **Restart IDE**:
   - Click `Restart IDE` when prompted

6. **Verify Installation**:
   - Open a `.dsm` file and verify syntax highlighting is working

## Configuration

### Code Style Settings

To customize indentation and formatting:

1. **Open Settings**: `Preferences` → `Editor` → `Code Style` → `DSM`

2. **Configure Indentation** (Tabs and Indents tab):
   - **Indent size**: Default 4 spaces
   - **Continuation indent**: Default 4 spaces
   - **Tab size**: Default 4
   - **Use tab character**: Disabled by default (uses spaces)

3. **Configure Spacing** (Spaces tab):
   - Space after comma: Enabled
   - Space before comma: Disabled
   - Space around assignment operators: Enabled

4. **Preview**: The preview pane shows a sample DSM file with your formatting settings applied

### DSM Validation Setup

To configure DSM validation with `dsm_util.py`:

1. **Open Settings**: `Preferences` → `Languages & Frameworks` → `DSM Language`

2. **Configure Paths**:
   - **Python Virtual Environment**: Path to your Python venv directory (e.g., `/Users/you/venv`)
   - **dsm_util.py Path**: `/path/to/viper/tools/dsm_util.py`
   - **Validation Timeout**: `5000` ms (default, used by Ctrl+Alt+V validation)

3. **Test Validation**:
   - Open a `.dsm` file with errors
   - Press `Ctrl+Alt+V` (or right-click → `Validate DSM Files`)
   - The DSM Validation tool window will open showing all errors
   - Errors will be highlighted with red wavy underlines in the editor

### Common Paths

**macOS/Linux:**
```
dsm_util.py: /Volumes/DigitalSubstrate/com.digitalsubstrate.viper/tools/dsm_util.py
Python:      /usr/bin/python3
```

**Windows:**
```
dsm_util.py: C:\Projects\viper\tools\dsm_util.py
Python:      C:\Python313\python.exe
```

## Usage Examples

### Creating a Simple Concept

```dsm
namespace MyApp {12345678-1234-1234-1234-123456789abc} {
    concept Person;

    struct PersonData {
        string name;
        uint32 age;
        optional<string> email;
    };

    attachment<Person, PersonData> data;
};
```

### Concept Inheritance

DSM supports single inheritance using the `is a` keyword:

```dsm
concept Material;

concept MaterialMatte is a Material;
concept MaterialMirror is a Material;
concept MaterialMultilayer is a Material;
```

**Navigation**: Use `Cmd+B` on `Material` in the derived concepts to jump to the base definition.

### Working with Collections

```dsm
struct Transform {
    vec<float, 3> position;      // Fixed-size vector
    vec<float, 3> rotation;
    vec<float, 3> scale;
};

struct SceneData {
    vector<key<Object>> objects;           // Dynamic vector
    map<string, key<Material>> materials;  // Map
    set<key<Light>> lights;                // Set
};
```

**Code Completion**: Type `vec` and press `Ctrl+Space` to see completion options.

### Function Pools and Attachment Function Pools

```dsm
// Regular function pool
function_pool Tools {dc9740c9-9d1d-4c1e-9caa-4c8843b91e82} {
    """Return a + b."""
    int64 add(int64 a, int64 b);

    """Return true if a is even."""
    bool isEven(int64 a);
};

// Attachment function pool (for collaborative editing)
attachment_function_pool ModelGraph {9bdcbb5b-76e9-426f-b8a6-a10ed2d949e6} {
    """Create a new vertex."""
    mutable key<Vertex> newVertex(key<Graph> graphKey, int64 value);

    """Delete selected elements."""
    mutable void deleteSelection(key<Graph> graphKey);

    """Get all selected vertices."""
    set<key<Vertex>> selectedVertices(key<Graph> graphKey);
};
```

**Structure View**: Open Structure View (`Cmd+7`) to see all function pools and their functions.

### Real-World Examples

Comprehensive DSM examples are available in the [dsm-samples](https://github.com/digital-substrate/dsm-samples) repository:

- **Graph modeling** (`Ge/`): Graph topology, vertices, edges, attachment function pools
- **3D rendering system** (`Re/`): Materials, cameras, lighting, timelines, sensors

## Keyboard Shortcuts

| Action | macOS | Windows/Linux |
|--------|-------|---------------|
| Validate DSM Files | `Ctrl+Alt+V` | `Ctrl+Alt+V` |
| Next Validation Error | `Alt+F2` | `Alt+F2` |
| Previous Validation Error | `Alt+Shift+F2` | `Alt+Shift+F2` |
| Reformat Code | `Cmd+Alt+L` | `Ctrl+Alt+L` |
| Go to Definition | `Cmd+B` | `Ctrl+B` |
| Find Usages | `Opt+F7` | `Alt+F7` |
| Type Hierarchy | `Ctrl+H` | `Ctrl+H` |
| Quick Documentation | `F1` | `Ctrl+Q` |
| Structure View | `Cmd+7` | `Alt+7` |
| Code Completion | `Ctrl+Space` | `Ctrl+Space` |

## Tips

1. **Use Structure View**: Navigate large DSM files efficiently with the hierarchical structure view
2. **Jump to Definition**: `Cmd+Click` on any type or concept to jump to its definition
3. **Find All Usages**: Right-click on a concept and select `Find Usages` to see where it's used
4. **Reformat Code**: Use `Cmd+Alt+L` to automatically format your DSM files with consistent indentation
5. **Validate Before Commit**: Run `Ctrl+Alt+V` to validate your DSM files before committing
6. **Use Validation Tool Window**: Double-click errors to jump directly to the problem location
7. **Cycle Through Errors**: Use `Alt+F2`/`Alt+Shift+F2` or toolbar buttons to navigate between errors
8. **Explore Examples**: Study the `dsm_samples/` directory for production-quality patterns
9. **Code Folding**: Use `Cmd+Plus/Minus` to collapse/expand code blocks
10. **Customize Formatting**: Configure indentation and spacing in `Preferences` → `Code Style` → `DSM`

## Troubleshooting

### Syntax Highlighting Not Working
- Verify the file extension is `.dsm`
- Check that the plugin is enabled: `Preferences` → `Plugins` → search for "DSM"
- Restart the IDE

### Validation Not Running
- Verify you've configured paths in `Preferences` → `Languages & Frameworks` → `DSM Language`
- Ensure `dsm_util.py` path is correct and file exists
- Verify Python virtual environment path is correct
- Try running validation manually with `Ctrl+Alt+V`
- Check IDE logs: `Help` → `Show Log in Finder/Explorer`
- Look for "DSM validation" entries in the logs

### Completion Not Working
- Try `Ctrl+Space` to manually trigger completion
- Ensure you're inside a valid context (namespace, struct, etc.)
- Check that the file parses without errors

### Navigation Not Working
- Ensure the target symbol is defined in the current file or imported files
- For cross-file navigation, ensure files are in the same project
- Try `Rebuild Project` if PSI cache is stale

## Requirements

- **JetBrains IDE**: Version 2024.3 or higher
  - IntelliJ IDEA Community/Ultimate
  - CLion
  - PyCharm Professional/Community
  - WebStorm, PhpStorm, RubyMine, GoLand, etc.
- **Platform Build**: 243 or higher
- **Python 3.x**: Required for external validation (optional feature)
- **DSM Tools**: `dsm_util.py` from Viper project (for validation)

## Known Issues

None at this time. Please report issues to: support@digitalsubstrate.io

## Development

For developers working on the plugin itself, see [CONTRIBUTING.md](CONTRIBUTING.md) for build instructions and code conventions.

## Support

For questions, bug reports, or feature requests:
- **Issues**: [GitHub Issues](https://github.com/digital-substrate/dsm-jetbrains/issues)
- **Email**: support@digitalsubstrate.io
- **Documentation**: DSM language documentation is distributed with the Viper DevKit

## Related Projects

- **VS Code Extension**: [dsm-vscode](https://github.com/digital-substrate/dsm-vscode) — DSM language support for Visual Studio Code
- **DSM Examples**: [dsm-samples](https://github.com/digital-substrate/dsm-samples) — Reference DSM models
- **DSM Utilities**: `dsm_util.py` — Command-line tool for DSM validation (distributed with Viper DevKit)
- **Viper Runtime**: Core C++ runtime for DSM-based applications

## License

This project is licensed under the MIT License — see [LICENSE](LICENSE).

---

**Digital Substrate** | [Report an issue](https://github.com/digital-substrate/dsm-jetbrains/issues) | [Contributing](CONTRIBUTING.md)
