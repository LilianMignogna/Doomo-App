package com.bddi.doomo.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R
import com.bddi.doomo.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {

    //lateinit var client: GoogleSignInClient
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private lateinit var btnSignIn: SignInButton
    private companion object {
        private const val TAG = "LoginActivity"
        private const val RC_GOOGLE_SIGN_IN = 4926
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*
        * Firebase auth : Google && Login/Password
         */

        auth = Firebase.auth

        btnSignIn = findViewById(R.id.sign_in_button)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        supportActionBar?.hide()

        val client = GoogleSignIn.getClient(this, gso)
        btnSignIn.setOnClickListener{
            val signInIntent = client.signInIntent
            startActivityForResult(signInIntent,  RC_GOOGLE_SIGN_IN)
        }

        val btnLogIn: MaterialButton = findViewById(R.id.button_validate)
        val etMail: EditText = findViewById(R.id.user_field)
        val etPassword: EditText = findViewById(R.id.user_password)
        val tvSignIn: TextView = findViewById(R.id.sign_in)

        btnLogIn.setOnClickListener{
            val email = etMail.text.toString()
            val password = etPassword.text.toString()

            if ( email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Email/password can not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Success! ", Toast.LENGTH_SHORT).show()
                    goMainActivity()
                } else {
                    Log.i(TAG, "signInWithEmail failed", task.exception)
                    Toast.makeText(this, "Authentification failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvSignIn.setOnClickListener{
            goToSignInActivity()
        }


    }

    private fun goToSignInActivity() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    private fun goMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            Log.w(TAG, "User is null, not going to navigate")
            return
        }

        // Navigate to MainActivity
        Log.d(TAG, "User is NOT null, going to navigate")
        goMainActivity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
}