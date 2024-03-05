package br.goldbach.activities.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class User(
    val name: String,
    val imageUri: String
)

object UserManager {
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val USER_IMAGE_URI_KEY = stringPreferencesKey("user_image_uri")
    private val USER_EXISTS_KEY = booleanPreferencesKey("user_exists")

    fun getCurrentUser(context: Context): Flow<User?> = runBlocking{
        val preferences = context.dataStore.data.first()
        val userExists = preferences[USER_EXISTS_KEY] ?: false

        if (userExists) {
            context.dataStore.data.map {
                User(
                    preferences[USER_NAME_KEY] ?: "Username",
                    preferences[USER_IMAGE_URI_KEY] ?: ""
                )
            }
        } else {
            flowOf(null)
        }
    }

    suspend fun createOrUpdateCurrentUser(context: Context, user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = user.name
            if (user.imageUri.isNotEmpty() || user.imageUri.isNotBlank()) {
                preferences[USER_IMAGE_URI_KEY] = user.imageUri
            }
            preferences[USER_EXISTS_KEY] = true
        }
    }
}