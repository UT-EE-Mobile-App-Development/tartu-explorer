package ee.ut.cs.tartu_explorer.feature.dev

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DevViewModel(context: Context) : ViewModel() {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _resetOnStart = MutableStateFlow(prefs.getBoolean(DatabaseProvider.KEY_RESET_ON_START, false))
    val resetOnStart = _resetOnStart.asStateFlow()

    fun onResetDatabaseOnce() {
        prefs.edit().putBoolean(DatabaseProvider.KEY_FORCE_RECREATE_ONCE, true).apply()
        // Note: The database will be reset on the *next* app start.
    }

    fun onToggleResetOnStart(shouldReset: Boolean) {
        prefs.edit().putBoolean(DatabaseProvider.KEY_RESET_ON_START, shouldReset).apply()
        _resetOnStart.update { shouldReset }
    }
}

class DevViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DevViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DevViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
