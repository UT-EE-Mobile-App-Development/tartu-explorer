package ee.ut.cs.tartu_explorer.core.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.appcompat.app.AppCompatDelegate

/**
 * ViewModel to manage the app theme including e.g. dark mode switching.
 */
class ThemeViewModel : ViewModel() {
    var isDarkMode = mutableStateOf(true)
        private set

    /**
     * Toggle and apply light/dark mode.
     *
     * Flips the [isDarkMode] value used to define our theme.
     */
    fun toggleDarkMode() {
        isDarkMode.value = !isDarkMode.value
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode.value) AppCompatDelegate.MODE_NIGHT_YES
            else androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}