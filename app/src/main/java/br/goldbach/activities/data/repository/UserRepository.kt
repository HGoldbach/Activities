package br.goldbach.activities.data.repository

import br.goldbach.activities.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getCurrentUser(): Flow<User?>
    suspend fun updateCurrentUser(user: User)
}