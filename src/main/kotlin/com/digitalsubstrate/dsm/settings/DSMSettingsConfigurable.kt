package com.digitalsubstrate.dsm.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Settings page for DSM Language Support plugin.
 * Accessible via: Settings → Languages & Frameworks → DSM Language
 */
class DSMSettingsConfigurable : Configurable {

    private var settingsPanel: JPanel? = null
    private val dsmUtilPathField = TextFieldWithBrowseButton()
    private val pythonInterpreterField = TextFieldWithBrowseButton()
    private val timeoutField = JBTextField()

    companion object {
        private const val DEFAULT_TIMEOUT_MS = 5000
    }

    init {
        // Setup file choosers
        setupFileChooser(
            pythonInterpreterField,
            isFolder = true,
            title = "Select Python Virtual Environment",
            description = "Choose the Python venv directory (will use bin/python3 or Scripts/python.exe)"
        )

        setupFileChooser(
            dsmUtilPathField,
            isFolder = false,
            title = "Select dsm_util.py",
            description = "Choose the location of dsm_util.py script",
            fileExtension = "py"
        )
    }

    /**
     * Helper function to setup a file or folder chooser for a text field.
     */
    private fun setupFileChooser(
        field: TextFieldWithBrowseButton,
        isFolder: Boolean,
        title: String,
        description: String,
        fileExtension: String? = null
    ) {
        field.addActionListener {
            val descriptor = when {
                isFolder -> FileChooserDescriptorFactory.createSingleFolderDescriptor()
                fileExtension != null -> FileChooserDescriptorFactory.createSingleFileDescriptor(fileExtension)
                else -> FileChooserDescriptorFactory.createSingleFileDescriptor()
            }
            descriptor.title = title
            descriptor.description = description

            val chooser = com.intellij.openapi.fileChooser.FileChooser.chooseFile(
                descriptor,
                null,
                null
            )
            chooser?.let {
                field.text = it.path
            }
        }
    }

    override fun getDisplayName(): String = "DSM Language"

    override fun createComponent(): JComponent {
        // Load current settings
        loadSettings()

        dsmUtilPathField.toolTipText = "Path to dsm_util.py script (required for validation)"
        pythonInterpreterField.toolTipText = "Python venv directory - auto-detects bin/python3 (Unix) or Scripts/python.exe (Windows)"
        timeoutField.toolTipText = "Maximum time in milliseconds to wait for validation to complete (used by Ctrl+Alt+V)"

        settingsPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel("dsm_util.py Path:"),
                dsmUtilPathField,
                1,
                false
            )
            .addLabeledComponent(
                JBLabel("Python Virtual Environment:"),
                pythonInterpreterField,
                1,
                false
            )
            .addLabeledComponent(
                JBLabel("Validation Timeout (ms):"),
                timeoutField,
                1,
                false
            )
            .addComponentFillVertically(JPanel(), 0)
            .panel

        return settingsPanel!!
    }

    override fun isModified(): Boolean {
        val settings = DSMSettingsState.getInstance()
        return dsmUtilPathField.text != settings.dsmUtilPath ||
                pythonInterpreterField.text != settings.pythonInterpreter ||
                timeoutField.text.toIntOrNull() != settings.validationTimeout
    }

    override fun apply() {
        val settings = DSMSettingsState.getInstance()
        settings.dsmUtilPath = dsmUtilPathField.text
        settings.pythonInterpreter = pythonInterpreterField.text
        settings.validationTimeout = timeoutField.text.toIntOrNull() ?: DEFAULT_TIMEOUT_MS
    }

    override fun reset() {
        loadSettings()
    }

    /**
     * Helper function to load settings from persistent state into UI fields.
     */
    private fun loadSettings() {
        val settings = DSMSettingsState.getInstance()
        dsmUtilPathField.text = settings.dsmUtilPath
        pythonInterpreterField.text = settings.pythonInterpreter
        timeoutField.text = settings.validationTimeout.toString()
    }

    override fun disposeUIResources() {
        settingsPanel = null
    }
}
