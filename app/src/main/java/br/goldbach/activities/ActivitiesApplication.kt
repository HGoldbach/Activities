package br.goldbach.activities

import android.app.Application
import br.goldbach.activities.data.model.User
import br.goldbach.activities.data.model.UserManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class ActivitiesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val userManager = UserManager

        CoroutineScope(Dispatchers.IO).launch {
            val currentUser = userManager.getCurrentUser(applicationContext).firstOrNull()

            if(currentUser == null) {
                val defaultUser = User("Username", "")
                userManager.createOrUpdateCurrentUser(applicationContext, defaultUser)
            }
        }
    }
}