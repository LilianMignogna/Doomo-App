package com.bddi.doomo.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    public companion object {
        private const val TAG = "SignInActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth
        val firebaseData = FirebaseDatabase.getInstance().reference

        val btnSignIn: MaterialButton = findViewById(R.id.button_validate)
        val etMail: EditText = findViewById(R.id.user_email)
        val etName: EditText = findViewById(R.id.user_username)
        val etAddress: EditText = findViewById(R.id.user_address)
        val etPassword1: EditText = findViewById(R.id.user_password)
        val etPassword2:EditText = findViewById(R.id.user_password_confirmation)

        btnSignIn.setOnClickListener{
            val email = etMail.text.toString()
            val name = etName.text.toString()
            val password1 = etPassword1.text.toString()
            val password2 = etPassword2.text.toString()
            val address = etAddress.text.toString()


            if ( email.isBlank() || password1.isBlank() || password2.isBlank() || name.isBlank() || address.isBlank()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if ( password1 != password2){
                Toast.makeText(this, "Veuillez confirmer votre mot de passe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {


                        var currentUser = auth.currentUser
                        if (currentUser == null ){
                            Toast.makeText(this , "User is null ", Toast.LENGTH_SHORT).show()
                            return@addOnCompleteListener
                        }

                        val users = db.collection("user")

                        val cuser = hashMapOf(
                            "name" to "$name",
                            "email" to "$email",
                            "address" to "$address"
                        )
                        users.document(currentUser.uid).set(cuser)

                        goLoginActivity()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "SignUp failed. Try again !",
                            Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                }
        }
    }

    private fun updateAddress(address: String, user: FirebaseUser) {
       /* println("UPDATE Address : $address")
        db.collection("user").document(user.uid)
            .update([address: address])*/
    }

    private fun goLoginActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}