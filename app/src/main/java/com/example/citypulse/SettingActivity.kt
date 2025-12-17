package com.example.citypulse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.citypulse.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val TAG = "SettingActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol Home (Kembali ke Peta)
        binding.navHome.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Tombol Logout (Hapus sesi & ke Login)
        binding.btnLogout.setOnClickListener {
            val sharedPrefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
            sharedPrefs.edit().putBoolean("IS_LOGGED_IN", false).apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Tombol Report Bug
        binding.btnReportBug.setOnClickListener {
            Log.d(TAG, "Report bug ditekan")
        }
    }
}