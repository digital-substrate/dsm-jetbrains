package com.digitalsubstrate.dsm.services

import com.digitalsubstrate.dsm.settings.DSMSettingsState
import com.digitalsubstrate.dsm.toolwindow.DSMValidationToolWindowFactory
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Service to run DSM validation using dsm_util.py.
 * Can be invoked manually or automatically on file save.
 */
@Service(Service.Level.PROJECT)
class DSMValidationService(private val project: Project) {

    private val log = Logger.getInstance(DSMValidationService::class.java)

    companion object {
        fun getInstance(project: Project): DSMValidationService {
            return project.getService(DSMValidationService::class.java)
        }
    }

    /**
     * Validate DSM files in the given directory.
     * Shows results in the tool window and adds editor highlighters.
     *
     * @param directory Directory to validate (usually project root or file directory)
     * @param showToolWindow Whether to show/focus the validation tool window
     */
    fun validateDirectory(directory: String, showToolWindow: Boolean = true) {
        val settings = DSMSettingsState.getInstance()

        log.info("DSM validation requested for: $directory")

        // Check if dsm_util.py is configured
        if (settings.dsmUtilPath.isEmpty()) {
            log.warn("DSM util path is empty!")
            if (showToolWindow) {
                DSMValidationToolWindowFactory.showError(
                    project,
                    "dsm_util.py path not configured. Please configure it in Settings → Languages & Frameworks → DSM Language"
                )
            }
            return
        }

        val dsmUtilFile = File(settings.dsmUtilPath)
        if (!dsmUtilFile.exists()) {
            if (showToolWindow) {
                DSMValidationToolWindowFactory.showError(
                    project,
                    "dsm_util.py not found at: ${settings.dsmUtilPath}"
                )
            }
            return
        }

        // Clear previous highlighters
        val highlighterService = DSMValidationHighlighterService.getInstance(project)
        highlighterService.clearHighlighters()

        // Run validation in background
        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Validating DSM Files...", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.text = "Running dsm_util.py check on $directory"
                val result = runDsmUtil(settings, directory, indicator)

                // Show results in tool window
                if (showToolWindow) {
                    DSMValidationToolWindowFactory.showResults(project, result)
                }

                // Add highlighters for errors in the editor
                if (result.status == ValidationResult.Status.ERROR && result.errors.isNotEmpty()) {
                    highlighterService.addHighlighters(result.errors)
                }
            }
        })
    }

    /**
     * Run dsm_util.py check command.
     */
    private fun runDsmUtil(
        settings: DSMSettingsState,
        directory: String,
        indicator: ProgressIndicator
    ): ValidationResult {
        try {
            val pythonInterpreter = resolvePythonExecutable(settings.pythonInterpreter)

            log.info("DSM validation: python=$pythonInterpreter, dsm_util=${settings.dsmUtilPath}, dir=$directory")

            val processBuilder = ProcessBuilder(
                pythonInterpreter,
                settings.dsmUtilPath,
                "check",
                directory
            )

            log.info("DSM validation command: ${processBuilder.command().joinToString(" ")}")

            val process = processBuilder.start()
            val output = process.inputStream.bufferedReader().readText()
            val errorOutput = process.errorStream.bufferedReader().readText()

            val finished = process.waitFor(settings.validationTimeout.toLong(), TimeUnit.MILLISECONDS)

            if (!finished) {
                process.destroyForcibly()
                log.warn("DSM validation TIMEOUT!")
                return ValidationResult.error("Validation timeout after ${settings.validationTimeout}ms")
            }

            val exitCode = process.exitValue()

            log.info("DSM validation exitCode=$exitCode")
            if (output.isNotEmpty()) {
                log.info("DSM validation stdout:\n$output")
            }
            if (errorOutput.isNotEmpty()) {
                log.warn("DSM validation stderr:\n$errorOutput")
            }

            if (indicator.isCanceled) {
                return ValidationResult.cancelled()
            }

            return if (exitCode == 0 && output.isEmpty()) {
                log.info("DSM validation: No errors found")
                ValidationResult.success()
            } else {
                log.info("DSM validation: Parsing output for errors")
                ValidationResult.fromOutput(output, directory)
            }

        } catch (e: Exception) {
            log.error("Error running dsm_util.py validation", e)
            return ValidationResult.error("Error running dsm_util.py: ${e.message}")
        }
    }

    /**
     * Resolve Python executable from venv directory or use system python.
     */
    private fun resolvePythonExecutable(venvPath: String): String {
        if (venvPath.isEmpty()) {
            return "python3"
        }

        val venvDir = File(venvPath)
        if (!venvDir.exists() || !venvDir.isDirectory) {
            log.warn("Venv path is not a valid directory: $venvPath, using system python3")
            return "python3"
        }

        // Try Unix/macOS structure
        val unixPython = File(venvDir, "bin/python3")
        if (unixPython.exists()) {
            return unixPython.absolutePath
        }

        // Try Windows structure
        val windowsPython = File(venvDir, "Scripts/python.exe")
        if (windowsPython.exists()) {
            return windowsPython.absolutePath
        }

        log.warn("No Python executable found in venv: $venvPath, using system python3")
        return "python3"
    }

    /**
     * Validation result data class.
     */
    data class ValidationResult(
        val status: Status,
        val errors: List<ValidationError> = emptyList(),
        val message: String? = null
    ) {
        enum class Status {
            SUCCESS, ERROR, CANCELLED
        }

        companion object {
            fun success() = ValidationResult(Status.SUCCESS, emptyList(), "No errors found")
            fun error(message: String) = ValidationResult(Status.ERROR, emptyList(), message)
            fun cancelled() = ValidationResult(Status.CANCELLED, emptyList(), "Validation cancelled")

            fun fromOutput(output: String, baseDirectory: String): ValidationResult {
                val errors = parseValidationOutput(output, baseDirectory)
                return if (errors.isEmpty()) {
                    success()
                } else {
                    ValidationResult(Status.ERROR, errors, "${errors.size} error(s) found")
                }
            }

            /**
             * Parse dsm_util.py output to extract validation errors.
             * Expected format: "path/filename.dsm:line:column: message"
             */
            private fun parseValidationOutput(output: String, baseDirectory: String): List<ValidationError> {
                val errors = mutableListOf<ValidationError>()
                val errorRegex = """^(.*?):(\d+):(\d+):\s*(.+)$""".toRegex()

                for (line in output.lines()) {
                    if (line.isBlank()) continue

                    val match = errorRegex.find(line)
                    if (match != null) {
                        val filePath = match.groupValues[1]
                        val lineNum = match.groupValues[2].toIntOrNull() ?: continue
                        val colNum = match.groupValues[3].toIntOrNull() ?: 0
                        val message = match.groupValues[4].trim()

                        errors.add(ValidationError(filePath, lineNum, colNum, message))
                    }
                }

                return errors
            }
        }
    }

    /**
     * Single validation error.
     */
    data class ValidationError(
        val filePath: String,
        val line: Int,
        val column: Int,
        val message: String
    )
}
