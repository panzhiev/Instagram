package com.panzhiev.instagram.activity

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.panzhiev.instagram.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(0) {

    private val TAG = "HomeActivity"
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()

        mAuth = FirebaseAuth.getInstance()
//        mAuth.signInWithEmailAndPassword("panzhiev.timur@gmail.com", "password")
//                .addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        Log.d(TAG, "signIn: success")
//                    } else {
//                        Log.e(TAG, "signIn: failure", it.exception)
//                    }
//                }
        mAuth.addAuthStateListener {
            if (mAuth.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        tv_sign_out.setOnClickListener {
            mAuth.signOut()
        }
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
