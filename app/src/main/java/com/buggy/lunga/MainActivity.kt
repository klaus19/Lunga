package com.buggy.lunga

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show splash for 2 seconds, then start main activity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainAppActivity::class.java))
            finish() // Close splash activity
        }, 2000) // 2 seconds delay
    }
}