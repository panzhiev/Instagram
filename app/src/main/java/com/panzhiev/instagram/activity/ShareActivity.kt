package com.panzhiev.instagram.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.panzhiev.instagram.R
import com.panzhiev.instagram.utils.CameraHelper
import com.panzhiev.instagram.utils.FirebaseHelper
import com.panzhiev.instagram.utils.GlideApp
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : BaseActivity(2) {

    private val TAG = "ShareActivity"
    private lateinit var mCamera: CameraHelper
    private lateinit var mFirebase: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        Log.d(TAG, "onCreate")

        mFirebase = FirebaseHelper(this)
        mCamera = CameraHelper(this)
        mCamera.takeCameraPicture()

        iv_back.setOnClickListener { finish() }
        tv_share.setOnClickListener { share() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCamera.REQUEST_CODE && resultCode == RESULT_OK) {
            GlideApp.with(this)
                    .load(mCamera.imageUri)
                    .centerCrop()
                    .into(iv_post)
        } else {
            finish()
        }
    }

    private fun share() {
        val imageUri = mCamera.imageUri
        if (imageUri != null) {
            val uid = mFirebase.auth.currentUser!!.uid
            mFirebase.storage
                    .child("users/${uid}/images")
                    .child(imageUri.lastPathSegment).putFile(imageUri)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            mFirebase.database.child("images").child(uid).push()
                                    .setValue(it.result.downloadUrl!!.toString())
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            startActivity(Intent(this, ProfileActivity::class.java))
                                            finish()
                                        }
                                    }
                        } else {
                            showToast(it.exception!!.message!!)
                        }
                    }
            //upload image to user folder
            //add image to user images
        }
    }
}
