package com.panzhiev.instagram.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.*
import com.panzhiev.instagram.R
import com.panzhiev.instagram.models.User
import com.panzhiev.instagram.utils.CameraHelper
import com.panzhiev.instagram.utils.FirebaseHelper
import com.panzhiev.instagram.utils.ValueEventListenerAdapter
import com.panzhiev.instagram.views.PasswordDialog
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity(), PasswordDialog.Listener {

    private val TAG = "EditProfileActivity"
    private lateinit var mUser: User
    private lateinit var mFirebase: FirebaseHelper
    private lateinit var mPendingUser: User
    private lateinit var mCamera: CameraHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        mCamera = CameraHelper(this)
        mFirebase = FirebaseHelper(this)

        close_image.setOnClickListener {
            finish()
        }
        save_image.setOnClickListener {
            updateProfile()
        }
        tv_change_photo.setOnClickListener { mCamera.takeCameraPicture() }

        mFirebase.currentUserReference()
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {

                    mUser = it.getValue(User::class.java)!!
                    et_name.setText(mUser.name)
                    et_username.setText((mUser.username))
                    et_website.setText((mUser.website))
                    et_bio.setText((mUser.bio))
                    et_email.setText((mUser.email))
                    et_phone.setText((mUser.phone))

                    iv_profile.loadUserPhoto(mUser.photo)
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCamera.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mFirebase.uploadUserPhoto(mCamera.imageUri!!) {
                val photoUrl = it.downloadUrl.toString()
                mFirebase.updateUserPhoto(photoUrl) {
                    mUser = mUser.copy(photo = photoUrl)
                    iv_profile.loadUserPhoto(mUser.photo)
                    Log.d(TAG, "onActivityResult: photo saved successfully")
                }
            }
        }
    }

    private fun updateProfile() {

        mPendingUser = readInputs()

        val error = validate(mPendingUser)
        if (error == null) {
            if (mPendingUser.email == mUser.email) {
                updateUser(mPendingUser)
            } else {
                //
                PasswordDialog().show(supportFragmentManager, "password_dialog")

            }
        } else {
            showToast(error)
        }
    }


    private fun readInputs(): User =
            User(
                    name = et_name.text.toString(),
                    username = et_username.text.toString(),
                    email = et_email.text.toString(),
                    website = et_website.text.toStringOrNull(),
                    bio = et_bio.text.toStringOrNull(),
                    phone = et_phone.text.toStringOrNull()
            )

    override fun onPasswordConfirm(password: String) {
        if (password.isEmpty()) {
            showToast("You should enter your password")
            return
        }
        //re-authenticate
        val credential = EmailAuthProvider.getCredential(mUser.email, password)
        mFirebase.reauthenticate(credential) {
            mFirebase.updateEmail(mPendingUser.email) {
                updateUser(mPendingUser)
            }
        }
    }

    private fun updateUser(user: User) {
        val updatesMap = mutableMapOf<String, Any?>()
        if (user.name != mUser.name) updatesMap["name"] = user.name
        if (user.username != mUser.username) updatesMap["username"] = user.username
        if (user.website != mUser.website) updatesMap["website"] = user.website
        if (user.bio != mUser.bio) updatesMap["bio"] = user.bio
        if (user.email != mUser.email) updatesMap["email"] = user.email
        if (user.phone != mUser.phone) updatesMap["phone"] = user.phone

        mFirebase.updateUser(updatesMap) {
            showToast("Profile saved")
            finish()
        }
    }

    private fun validate(user: User): String? =
            when {
                user.name.isEmpty() -> "Please enter name"
                user.username.isEmpty() -> "Please enter username"
                user.email.isEmpty() -> "Please enter email"
                else -> null
            }
}
