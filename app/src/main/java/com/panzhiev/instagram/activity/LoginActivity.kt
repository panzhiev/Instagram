package com.panzhiev.instagram.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.panzhiev.instagram.R
import kotlinx.android.synthetic.main.activity_login.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class LoginActivity : AppCompatActivity(), KeyboardVisibilityEventListener, TextWatcher, View.OnClickListener {

    private val TAG = "LoginActivity"
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        KeyboardVisibilityEvent.setEventListener(this, this)
        btn_login.isEnabled = false
        et_email.addTextChangedListener(this)
        et_password.addTextChangedListener(this)
        btn_login.setOnClickListener(this)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onVisibilityChanged(isOpen: Boolean) {
        if (isOpen) {
            scroll_view.scrollTo(0, scroll_view.bottom)
            tv_create_account.visibility = View.GONE
        } else {
            scroll_view.scrollTo(0, scroll_view.top)
            tv_create_account.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View) {
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        if (validate(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if (it.isSuccessful){
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            }
        } else {
            showToast("Please enter email and password")
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        btn_login.isEnabled = validate(et_email.text.toString(), et_password.text.toString())
    }

    private fun validate(email: String, password: String) =
            email.isNotEmpty() && password.isNotEmpty()
}
