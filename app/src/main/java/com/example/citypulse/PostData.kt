package com.example.citypulse
data class PostData(
    val id: String? = null,
    val deskripsi: String = "",
    val kategori: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val pembuat: String = "" // Nama atau Username pelapor
)