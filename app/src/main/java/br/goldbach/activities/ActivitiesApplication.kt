package br.goldbach.activities

import android.app.Application
import br.goldbach.activities.data.model.UserManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class ActivitiesApplication : Application() {}