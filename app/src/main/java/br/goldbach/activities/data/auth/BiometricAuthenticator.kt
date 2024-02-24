package br.goldbach.activities.data.auth


import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity

interface BiometricAuthenticator {
    fun isBiometricAuthAvailable() : BiometricAuthStatus
    fun promptBiometricAuth(
        title: String,
        subTitle: String,
        negativeButtonText: String,
        fragmentActivity: FragmentActivity,
        onSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
        onFailed: () -> Unit,
        onError: (errorCode: Int, errorMessage: String) -> Unit
    )
}