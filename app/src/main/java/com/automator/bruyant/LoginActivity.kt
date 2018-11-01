package com.automator.bruyant

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object {
        private val TAG = LoginActivity::class.java.simpleName
        private const val USERNAME = "Smith"
        private const val PASSWORD = "s1234567"
        private const val USERNAME1 = "A"
        private const val PASSWORD1 = "b"
    }

    private fun animateText() {
        val scale = 1.5f
        val duration: Long = 150
        loginStatus.animate().scaleX(scale).scaleY(scale).setDuration(duration).withEndAction {
            loginStatus.animate().scaleX(1.0f).scaleY(1.0f).setDuration(duration)
        }
    }

    private fun getSharedPrefs(): SharedPreferences {
        return this.getSharedPreferences("login", Context.MODE_PRIVATE)
    }

    private fun successfulLogin() {
        val prefs = getSharedPrefs()
        prefs.edit().putBoolean("isLoggedIn", true).apply()
    }

    private fun startMain() {
        try {
            startActivity(Intent(this.applicationContext, MainActivity::class.java))
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val prefs = getSharedPrefs()
        if (prefs.getBoolean("isLoggedIn", false)) {
            startMain()
            return
        }
        login.setOnClickListener {
            val pwd = password.text.toString()
            val uname = username.text.toString()
            val combo1 = uname == USERNAME && pwd == PASSWORD
            val combo2 = uname == USERNAME1 && pwd == PASSWORD1
            if (combo1 || combo2) {
                loginStatus.setTextColor(Color.GREEN)
                loginStatus.text = getString(R.string.success_login)
                successfulLogin()
                animateText()
                loginStatus.postDelayed({
                    startMain()
                }, 1200)
            } else {
                loginStatus.setTextColor(Color.RED)
                loginStatus.text = getString(R.string.retry_login)
                animateText()
            }
        }
    }
}