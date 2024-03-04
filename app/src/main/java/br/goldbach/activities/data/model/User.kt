package br.goldbach.activities.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class User(
    val name: String,
    val imageUri: String
)

object UserManager {
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val USER_IMAGE_URI_KEY = stringPreferencesKey("user_image_uri")

    suspend fun getCurrentUser(context: Context) : Flow<User> {
        val preferences = context.dataStore.data.first()
        return context.dataStore.data.map {
            User(
                preferences[USER_NAME_KEY] ?: "Username",
                preferences[USER_IMAGE_URI_KEY] ?: ""
            )
        }
    }

    suspend fun updateCurrentUser(context: Context, user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = user.name
            if(user.imageUri.isNotEmpty() || user.imageUri.isNotBlank()) {
                preferences[USER_IMAGE_URI_KEY] = user.imageUri
            }
        }
    }
}