package sid.com.quotely

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import sid.com.quotely.contents.Home
import sid.com.quotely.utils.AppShortcutUtils
import sid.com.quotely.utils.AuthUtils
import sid.com.quotely.utils.RealtimeDatabaseUtils
import sid.com.quotely.utils.StringUtils

class SignIn : AppCompatActivity() {

    private val USER_ID = "USER_ID"
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        AppShortcutUtils.setUpAppShortcuts(this)
        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            val uid = auth.currentUser!!.uid
            goToHome(uid)
            writeUser()
        } else {

            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )


            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setTheme(R.style.FirebaseUITheme)
                    .setLogo(R.drawable.ic_quotely_logo)
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(true)
                    .build(),
                RC_SIGN_IN
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(
                    this,
                    StringUtils.welcomeMessage(user?.displayName ?: ""),
                    Toast.LENGTH_SHORT
                ).show()

                writeUser()
                goToHome(user!!.uid)

            } else {
                FirebaseCrashlytics.getInstance()
                    .log("FirebaseAuth UI Sign-In Checks --> Result Code Not Okay --> $resultCode ")
            }
        }
    }

    private fun writeUser() {
        RealtimeDatabaseUtils.writeUserToFirebaseDatabase(AuthUtils.getUserMeta())
    }

    private fun goToHome(uid: String) {
        val intent = Intent(this, Home::class.java)
        intent.putExtra(USER_ID, uid)
        startActivity(intent)
        finish()
    }

}
