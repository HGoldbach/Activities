package br.goldbach.activities.data.repository


import android.content.Context
import br.goldbach.activities.data.model.User
import br.goldbach.activities.data.model.UserManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userManager: UserManager,
    @ApplicationContext private val context: Context
) : UserRepository {

    override suspend fun getCurrentUser(): Flow<User> {
        return userManager.getCurrentUser(context)
    }

    override suspend fun updateCurrentUser(user: User) {
        userManager.updateCurrentUser(context, user)
    }
}