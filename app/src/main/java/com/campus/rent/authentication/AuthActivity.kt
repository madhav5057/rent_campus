package com.campus.rent.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.campus.rent.R
import com.campus.rent.database.model.User
import com.campus.rent.databinding.ActivityAuthenticationBinding
import com.campus.rent.mainActivity.MainActivity
import com.campus.rent.utils.Constants.TAG
import com.campus.rent.utils.NetworkResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAuthenticationBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val authViewModel by viewModels<AuthViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        binding.btnSignIn.setOnClickListener {
            val signInClient = googleSignInClient.signInIntent
            launcher.launch(signInClient)
        }

        authViewModel.userResponseLiveData.observe(this) {

            when (it) {
                is NetworkResult.Success -> {
                    startActivity(Intent(this, MainActivity::class.java))

                }

                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                }
            }

        }

    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {

                            val userId = auth.currentUser?.uid.toString()
                            val userName = auth.currentUser?.displayName.toString()
                            val userEmail = auth.currentUser?.email.toString()
                            val userProfileImage = auth.currentUser?.photoUrl.toString()



                            authViewModel.registerUser(
                                User(
                                    userId,
                                    userName,
                                    userEmail,
                                    userProfileImage,
                                    ""
                                )
                            )

                        } else {
                            Log.d(TAG, "Failed at gso")
                        }
                    }
                } else {
                    Log.d(TAG, "Failed at gso")

                }
            } else {
                Log.d(TAG, "Failed to load gso")
            }
        }


    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


}