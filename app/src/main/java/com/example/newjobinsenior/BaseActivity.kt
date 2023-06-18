package com.example.newjobinsenior

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    var authMenuItem: MenuItem? = null

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
        return super.onOptionsItemSelected(item)
    }
}
