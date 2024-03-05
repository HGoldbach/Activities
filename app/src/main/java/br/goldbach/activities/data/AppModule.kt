package br.goldbach.activities.data

import android.content.Context
import br.goldbach.activities.data.auth.BiometricAuthenticator
import br.goldbach.activities.data.auth.BiometricAuthenticatorImpl
import br.goldbach.activities.data.dao.ActivitiesDao
import br.goldbach.activities.data.db.ActivitiesDatabase
import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.data.model.UserManager
import br.goldbach.activities.data.repository.ActivitiesRepository
import br.goldbach.activities.data.repository.ActivitiesRepositoryImpl
import br.goldbach.activities.data.repository.UserRepository
import br.goldbach.activities.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideBiometricAuthenticator(@ApplicationContext appContext: Context) : BiometricAuthenticator {
        return BiometricAuthenticatorImpl(appContext)
    }

    @Singleton
    @Provides
    fun provideActivitiesRepository(activitiesDao: ActivitiesDao) : ActivitiesRepository {
        return ActivitiesRepositoryImpl(activitiesDao)
    }

    @Singleton
    @Provides
    fun provideUserRepository(@ApplicationContext appContext: Context, userManager: UserManager) : UserRepository {
        return UserRepositoryImpl(context = appContext, userManager = userManager)
    }

    @Singleton
    @Provides
    fun provideActivitiesDatabase(@ApplicationContext appContext: Context) : ActivitiesDao {
        return ActivitiesDatabase.getDatabase(appContext).ActivitiesDao()
    }

    @Singleton
    @Provides
    fun provideUserManager() : UserManager {
        return UserManager
    }

}