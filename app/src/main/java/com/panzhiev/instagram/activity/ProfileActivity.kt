package com.panzhiev.instagram.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.AudioAttributesCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.panzhiev.instagram.R
import com.panzhiev.instagram.models.User
import com.panzhiev.instagram.utils.FirebaseHelper
import com.panzhiev.instagram.utils.GlideApp
import com.panzhiev.instagram.utils.ValueEventListenerAdapter
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.jar.Attributes

class ProfileActivity : BaseActivity(4) {

    private lateinit var mFirebase: FirebaseHelper

    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()
        btn_edit_profile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        mFirebase = FirebaseHelper(this)
        mFirebase.currentUserReference()
                .addValueEventListener(ValueEventListenerAdapter {
                    mUser = it.getValue(User::class.java)!!
                    iv_profile.loadUserPhoto(mUser.photo)
                    tv_username.text = mUser.username
                })

        rv_profile_images.layoutManager = GridLayoutManager(this, 3)
        mFirebase.database.child("images").child(mFirebase.auth.currentUser!!.uid)
                .addValueEventListener(ValueEventListenerAdapter {
                    val images = it.children.map { it.getValue(String::class.java)!! }
                    rv_profile_images.adapter = ImagesAdapter(images + images + images)
                })
    }
}

class ImagesAdapter(private val images: List<String>) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    class ViewHolder(val image: ImageView) : RecyclerView.ViewHolder(image)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val image = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false) as ImageView
        return ViewHolder(image)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.loadImage(images[position])
    }

    private fun ImageView.loadImage(image: String) {
        GlideApp.with(this)
                .load(image)
                .centerCrop()
                .into(this)
    }
}

class SquareImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
