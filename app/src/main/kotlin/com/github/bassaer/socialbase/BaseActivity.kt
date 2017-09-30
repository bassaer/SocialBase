package com.github.bassaer.socialbase

import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {

    private var progressBar: ProgressBar = ProgressBar(this)

    fun showProgressBar() {
        this.progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        if (this.progressBar.isShown) {
            this.progressBar.visibility = View.GONE
        }
    }

    fun getUid() = FirebaseAuth.getInstance().currentUser?.uid
}
