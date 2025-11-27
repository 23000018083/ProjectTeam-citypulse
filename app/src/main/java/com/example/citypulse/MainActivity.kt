package com.example.citypulse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.citypulse.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate: MainActivity dibuat")

        // Mengatur padding agar UI tidak tertutup status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔹 Ketika tombol Sign Up ditekan, pindah ke halaman SettingActivity
        binding.btnSignUp.setOnClickListener {
            Log.d(TAG, "Tombol Sign Up ditekan → pindah ke SettingActivity")
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: MainActivity mulai terlihat di layar")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: MainActivity aktif dan siap digunakan")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: MainActivity dijeda (akan membuka activity lain)")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: MainActivity sudah tidak tampil di layar")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: MainActivity muncul kembali setelah berhenti")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: MainActivity dihapus dari memori")
    }
}
