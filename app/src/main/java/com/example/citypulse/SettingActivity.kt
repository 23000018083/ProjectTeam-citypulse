package com.example.citypulse

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

        Log.d(TAG, "onCreate: SettingActivity dibuat")

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            Log.d(TAG, "Logout ditekan")
            finish() // Kembali ke MainActivity
        }

        // Tombol Report Bug
        binding.btnReportBug.setOnClickListener {
            Log.d(TAG, "Report bug ditekan")
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: SettingActivity mulai terlihat di layar")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: SettingActivity aktif dan bisa berinteraksi")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: SettingActivity akan berhenti sementara (misalnya pindah activity)")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: SettingActivity sudah tidak tampil di layar")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: SettingActivity dihapus dari memori")
    }
}
