package com.example.citypulse

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.citypulse.databinding.ActivityStatusBinding
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.io.InputStream

class StatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatusBinding
    private var imageUri: Uri? = null
    private var curLat: Double = 0.0
    private var curLng: Double = 0.0

    private val database = FirebaseDatabase.getInstance().getReference("posts")

    // Launcher untuk Galeri
    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            binding.ivPreview.visibility = View.VISIBLE
            binding.ivPreview.setImageURI(uri)
        }
    }

    // Launcher untuk Ambil Lokasi Manual (PickLocationActivity)
    private val startPickLocation = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                curLat = data.getDoubleExtra("LAT", 0.0)
                curLng = data.getDoubleExtra("LNG", 0.0)

                // Update teks menjadi statis agar tetap rapi sesuai gambar
                binding.tvLokasi.text = "Lokasi Dipilih"
                Toast.makeText(this, "Lokasi berhasil diperbarui", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Ambil Nama User
        val sharedPrefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val userId = sharedPrefs.getString("CURRENT_USER_ID", "")

        if (!userId.isNullOrEmpty()) {
            FirebaseDatabase.getInstance().getReference("users").child(userId)
                .get().addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(UserModel::class.java)
                    if (user != null) {
                        binding.tvNamaAkun.text = user.nama
                    }
                }
        }

        // 2. Ambil Lokasi Terkini Otomatis (Default Awal)
        fetchCurrentLocation()

        // 3. Listeners
        binding.btnBack.setOnClickListener { finish() }

        // Klik pada teks "Pilih Lokasi" untuk buka peta
        binding.tvLokasi.setOnClickListener {
            val intent = Intent(this, PickLocationActivity::class.java)
            startPickLocation.launch(intent)
        }

        binding.btnPilihGambar.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.btnPosting.setOnClickListener {
            processAndUpload()
        }
    }

    private fun fetchCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    curLat = location.latitude
                    curLng = location.longitude
                    // Tetapkan teks awal
                    binding.tvLokasi.text = "Pilih Lokasi"
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }
    }

    private fun processAndUpload() {
        val deskripsi = binding.etStatus.text.toString().trim()
        val kategori = binding.spinnerKategori.selectedItem.toString()

        if (deskripsi.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Lengkapi deskripsi dan gambar!", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnPosting.isEnabled = false
        binding.btnPosting.text = "Mengunggah..."

        val base64Image = encodeImageToBase64(imageUri!!)

        if (base64Image != null) {
            saveToDatabase(deskripsi, kategori, base64Image)
        } else {
            binding.btnPosting.isEnabled = true
            binding.btnPosting.text = "POSTING"
            Toast.makeText(this, "Gagal memproses gambar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun encodeImageToBase64(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            // Kompresi 30% agar tidak memberatkan database
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            val byteArray = outputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveToDatabase(deskripsi: String, kategori: String, imageBase64: String) {
        val postId = database.push().key
        val post = PostData(
            id = postId,
            deskripsi = deskripsi,
            kategori = kategori,
            latitude = curLat,
            longitude = curLng,
            imageUrl = imageBase64,
            pembuat = binding.tvNamaAkun.text.toString(),
            timestamp = System.currentTimeMillis()
        )

        if (postId != null) {
            database.child(postId).setValue(post).addOnCompleteListener {
                Toast.makeText(this, "Posting Berhasil!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}