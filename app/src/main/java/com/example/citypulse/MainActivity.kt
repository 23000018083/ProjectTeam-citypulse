package com.example.citypulse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Inisialisasi Splash Screen (Wajib sebelum super.onCreate)
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 2. Jalankan logika pengecekan
        if (checkLoginStatus()) {
            // Jika sudah login -> Ke Dashboard Maps
            navigateToMaps()
        } else {
            // Jika belum login -> Ke Login
            navigateToLogin()
        }

        // 3. PENTING: Matikan MainActivity agar Splash tidak tertahan di layar
        finish()
    }

    private fun checkLoginStatus(): Boolean {
        val sharedPrefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("IS_LOGGED_IN", false)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMaps() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}