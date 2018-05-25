package com.panzhiev.instagram.views

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.panzhiev.instagram.R
import kotlinx.android.synthetic.main.dialog_password.view.*

class PasswordDialog : DialogFragment() {

    private lateinit var mListener: Listener

    interface Listener {
        fun onPasswordConfirm(password: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as Listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_password, null)
        return AlertDialog.Builder(context!!)
                .setView(view)
                .setPositiveButton(android.R.string.ok, { dialog, which ->
                    mListener.onPasswordConfirm(view.et_password.text.toString())
                    //send password to activity
                })
                .setNegativeButton(android.R.string.cancel, { dialog, which ->
                })
                .setTitle(R.string.please_enter_password)
                .create()
    }
}