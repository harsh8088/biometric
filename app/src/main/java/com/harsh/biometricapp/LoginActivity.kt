package com.harsh.biometricapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var biometricManager: BiometricManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        biometricManager = BiometricManager.from(this)
        verifyingBioMetricExistence()
    }

    private fun verifyingBioMetricExistence() {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                print("App can authenticate using biometrics.")
                setBioMetric()
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                print("No biometric features available on this device.")
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                print("Biometric features are currently unavailable.")
                Toast.makeText(
                    applicationContext,
                    "Biometric features are currently unavailable.", Toast.LENGTH_SHORT
                )
                    .show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                print("The user hasn't associated any biometric credentials with their account.")
                Toast.makeText(
                    applicationContext,
                    "The user hasn't associated any biometric credentials with their account.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
    }

    private fun setBioMetric() {
        executor =
            ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)

                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()
                    startHomeActivity()

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            // Can't call setNegativeButtonText() and
            // setAllowedAuthenticators(... or DEVICE_CREDENTIAL) at the same time.
            .setNegativeButtonText("Cancel")
            .build()
    }

    fun onBioMetricClick(view: View) {
        biometricPrompt.authenticate(promptInfo)
    }

    fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
//        intent.putExtra("keyIdentifier", value)
        startActivity(intent)
    }
}