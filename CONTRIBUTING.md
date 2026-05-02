# Contributing to DSM JetBrains Plugin

Thanks for your interest in contributing.

## Reporting issues

Use [GitHub Issues](https://github.com/digital-substrate/dsm-jetbrains/issues) and pick the appropriate template (bug report or feature request).

## Submitting pull requests

1. Fork the repository and create a feature branch from `main`
2. Make your changes (see "Building" and "Code conventions" below)
3. Verify compilation: `./gradlew compileKotlin`
4. Open a pull request with a clear description of what changed and why

## Building

```bash
./gradlew compileKotlin          # Compile
./gradlew runIde --no-daemon     # Test in IDE sandbox
./gradlew clean build            # Full rebuild
```

Requires JDK 17+ and an active internet connection for the first build (dependency download).

## Code conventions

1. **PSI tree over text scanning**

   Use `element.node.findChildByType(DSMTypes.X)`. Never `element.containingFile.text` with regex.

   Reference: `src/main/kotlin/com/digitalsubstrate/dsm/psi/DSMPsiUtil.kt`

2. **IntelliJ docs first**

   Before implementing a feature, check the [IntelliJ Platform SDK docs](https://plugins.jetbrains.com/docs/intellij/) and look at how the Kotlin or Rust plugins solve similar problems.

3. **Indexes over file scanning**

   Use `FileBasedIndex` / `StubIndex`. Never `FilenameIndex.getAllFilesByExt()` in hot paths.

## Reference files

- Element factory: `src/main/kotlin/com/digitalsubstrate/dsm/psi/DSMElementFactory.kt`
- Grammar: `src/main/grammar/DSM.bnf`
- PSI utilities: `src/main/kotlin/com/digitalsubstrate/dsm/psi/DSMPsiUtil.kt`

## License

This project is licensed under the MIT License (see [LICENSE](LICENSE)). By submitting a pull request, you agree that your contribution is provided under the same license (inbound = outbound). No CLA is required.
