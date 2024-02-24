package br.goldbach.activities.ui.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import br.goldbach.activities.R
import br.goldbach.activities.data.auth.BiometricAuthenticator
import br.goldbach.activities.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var biometricAuthenticator: BiometricAuthenticator
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.parseColor("#12E1D9")
        }

        binding.button.setOnClickListener {
            biometricAuthenticator.promptBiometricAuth(
                title = "Login",
                subTitle = "Use your fingerprint to log in",
                negativeButtonText = "Cancel",
                fragmentActivity = this,
                onSuccess = {
                    val intent = Intent(this@MainActivity, EntryActivity::class.java)
                    startActivity(intent)
                },
                onFailed = {
                    Toast.makeText(
                        this@MainActivity,
                        "Wrong fingerprint or face id",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onError = { _, error ->
                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                }

            )
        }
    }
}