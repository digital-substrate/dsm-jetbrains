# DSM JetBrains Plugin - Development Guidelines

## Build Commands
```bash
dsm_jetbrains/gradlew -p dsm_jetbrains compileKotlin          # Compile
dsm_jetbrains/gradlew -p dsm_jetbrains runIde --no-daemon     # Test in IDE
dsm_jetbrains/gradlew -p dsm_jetbrains clean build            # Full rebuild
```

## Absolute Rules

1. **PSI tree over text scanning**
   - Use `element.node.findChildByType(DSMTypes.X)`
   - Never `element.containingFile.text` with regex
   - Reference: `dsm_jetbrains/src/main/kotlin/com/digitalsubstrate/dsm/psi/DSMPsiUtil.kt`

2. **IntelliJ docs before implementation**
   - SDK: https://plugins.jetbrains.com/docs/intellij/
   - Check how Kotlin/Rust plugins do it

3. **Indexes over file scanning**
   - Use `FileBasedIndex` / `StubIndex`
   - Never `FilenameIndex.getAllFilesByExt()` in hot paths

## Reference Files
- PSI patterns: `dsm_jetbrains/src/main/kotlin/com/digitalsubstrate/dsm/psi/DSMPsiUtil.kt`
- Element factory: `dsm_jetbrains/src/main/kotlin/com/digitalsubstrate/dsm/psi/DSMElementFactory.kt`
- Grammar: `dsm_jetbrains/src/main/grammar/DSM.bnf`

## Workflows (just-in-time)
- `/jbp-implement` - New feature implementation
- `/jbp-diagnose` - Failure investigation protocol

## Status & Roadmap
See `dsm_jetbrains/DSM_PLUGIN_STATUS.md` for features, technical debt, and roadmap.
