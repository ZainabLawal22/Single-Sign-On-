package com.example.singlesignon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.credentials.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class MainActivity : AppCompatActivity() {
    // Define the URL of the login page for SSO
    //private val loginUrl = "https://www.google.com"
    private lateinit var oneTapClient: OneTapClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        oneTapClient = OneTap.getClient(this)


val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            showOneTapUI()
           /* // Create an intent to launch the custom tab with the login page URL
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.intent.putExtra(
                Intent.EXTRA_REFERRER,
                Uri.parse("android-app://$packageName")
            )
            customTabsIntent.launchUrl(this, Uri.parse(loginUrl))*/
        }


    }

    private fun showOneTapUI() {
        val request = createOneTapRequest()

        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.white))
        builder.setShowTitle(true)

        val customTabsIntent = builder.build()
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.launchUrl(this, Uri.parse(request))
    }

    private fun createOneTapRequest(): String {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()

        val credentialRequest = CredentialRequest.Builder()
            .setPasswordLoginSupported(false)
            .setAccountTypes(IdentityProviders.GOOGLE)
            .setGoogleIdToken(googleSignInOptions.serverClientId)
            .build()

        val hintRequest = HintRequest.Builder()
            .setHintPickerConfig(
                CredentialPickerConfig.Builder()
                    .setShowCancelButton(true)
                    .build()
            )
            .setCredentialRequest(credentialRequest)
            .build()

        return oneTapClient.createRequest(hintRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == OneTap.REQUEST_CODE_ONE_TAP) {
            val credential = data?.getParcelableExtra<Credential>(Credential.EXTRA_KEY)

            if (credential != null) {
                // The user has successfully signed in
                // You can use the credential to sign in the user to your app

            } else {
                // The user canceled or didn't select any credential
                // You can handle this case as appropriate for your app
            }
        }


    }
}


