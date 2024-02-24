package br.goldbach.activities.data

import android.content.Context
import br.goldbach.activities.data.auth.BiometricAuthenticator
import br.goldbach.activities.data.auth.BiometricAuthenticatorImpl
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

}