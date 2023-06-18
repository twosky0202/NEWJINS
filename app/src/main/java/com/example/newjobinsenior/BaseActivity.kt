package com.example.newjobinsenior

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

open class BaseActivity : AppCompatActivity() {
    var authMenuItem: MenuItem? = null

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        setAppBackgroundColor()
        setAppTextColor()
    }
    fun setAppBackgroundColor() {
        val bgColor: String? = sharedPreferences.getString("bg_color", "")
        if (bgColor != null && bgColor.isNotEmpty() && bgColor.startsWith("#")) {
            window.decorView.setBackgroundColor(Color.parseColor(bgColor))
        }
    }
    fun setAppTextColor() {
        val txColor: String? = sharedPreferences.getString("tx_color", "")
        if (txColor != null && txColor.isNotEmpty() && txColor.startsWith("#")) {
            val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
            recursivelySetTextColor(viewGroup, Color.parseColor(txColor))
        }
    }

    private fun recursivelySetTextColor(view: View, color: Int) {
        if (view is TextView && view !is Button) {
            view.setTextColor(color)
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                recursivelySetTextColor(view.getChildAt(i), color)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        authMenuItem = menu?.findItem(R.id.menu_auth)
        if (MyApplication.checkAuth()) {
            authMenuItem?.title = "${MyApplication.email}님"
        } else {
            authMenuItem?.title = "로그인"
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()

        if (authMenuItem != null) {
            if (MyApplication.checkAuth()) {
                authMenuItem?.title = "${MyApplication.email}님"
            } else {
                authMenuItem?.title = "로그인"
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_auth) {
            val intent = Intent(this, AuthActivity::class.java)

            if (item.title == "로그인") {
                intent.putExtra("data", "logout")
            } else {
                intent.putExtra("data", "login")
            }
            startActivity(intent)
        }
        if(item.itemId == R.id.menu_main_setting){
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        setAppBackgroundColor()
        setAppTextColor()
    }
}
