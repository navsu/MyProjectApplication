package com.memandis.appservice.main.domain.repository.prefs

import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//import javax.inject.Singleton
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.memandis.appservice.main.domain.repository.prefs.DataStorePreferenceStorage.PreferencesKeys.PREF_ONBOARDING
import kotlinx.coroutines.flow.map

interface PreferenceStorage {
    suspend fun completeOnboarding(complete: Boolean)
    val onboardingCompleted: Flow<Boolean>
}

//@Singleton
class DataStorePreferenceStorage
    (
//@Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceStorage {
    companion object {
        const val PREFS_NAME = "mydezigner"
    }

    object PreferencesKeys {
        val PREF_ONBOARDING = booleanPreferencesKey("pref_onboarding")

    }

    override suspend fun completeOnboarding(complete: Boolean) {
        dataStore.edit {
            it[PREF_ONBOARDING] = complete
        }
    }
    override val onboardingCompleted: Flow<Boolean> =
        dataStore.data.map { it[PREF_ONBOARDING] ?: false }

}