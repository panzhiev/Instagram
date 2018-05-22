package com.panzhiev.instagram.activity

import android.content.Intent
import android.os.Bundle
import com.panzhiev.instagram.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity(4) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()
        btn_edit_profile.setOnClickListener{
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
    }
}
