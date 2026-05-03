package com.digitalsubstrate.dsm.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Persistent state for DSM plugin settings.
 * Stores configuration like path to dsm_util.py and validation preferences.
 */
@Service
@State(
    name = "DSMSettingsState",
    storages = [Storage("DSMSettings.xml")]
)
class DSMSettingsState : PersistentStateComponent<DSMSettingsState> {

    /**
     * Path to dsm_util.py script for external validation.
     * Can be:
     * - Absolute path to dsm_util.py
     * - Empty string (will trigger auto-detection)
     */
    var dsmUtilPath: String = ""

    /**
     * Path to Python interpreter (python3, python, or custom path).
     * Default: "python3"
     */
    var pythonInterpreter: String = "python3"

    /**
     * Timeout for dsm_util.py execution in milliseconds.
     * Default: 5000ms (5 seconds)
     */
    var validationTimeout: Int = 5000

    override fun getState(): DSMSettingsState = this

    override fun loadState(state: DSMSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): DSMSettingsState {
            return ApplicationManager.getApplication().getService(DSMSettingsState::class.java)
        }
    }
}
