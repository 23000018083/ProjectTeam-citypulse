package com.example.citypulse

import android.graphics.BitmapFactory
import android.graphics.Color // Import untuk warna dinamis
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var postsList: MutableList<PostData> = mutableListOf()

    fun submitList(posts: List<PostData>) {
        postsList.clear()
        postsList.addAll(posts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postsList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = postsList.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtPostStatus: TextView = itemView.findViewById(R.id.txtPostStatus)
        private val imgPost: ImageView = itemView.findViewById(R.id.imgPost)
        private val txtNamaPembuat: TextView = itemView.findViewById(R.id.txtNamaPembuat)
        // Tambahkan inisialisasi ID kategori di sini
        private val txtKategoriPost: TextView = itemView.findViewById(R.id.txtKategoriPost)

        fun bind(post: PostData) {
            txtPostStatus.text = post.deskripsi
            txtNamaPembuat.text = post.pembuat

            // 1. GET DATA KATEGORI: Mengambil teks dari database
            txtKategoriPost.text = "Kategori: ${post.kategori}"

            // 2. LOGIKA WARNA (Opsional): Biar tampilan lebih informatif
            when (post.kategori) {
                "Rendah" -> txtKategoriPost.setTextColor(Color.parseColor("#4CAF50")) // Hijau
                "Sedang" -> txtKategoriPost.setTextColor(Color.parseColor("#FF9800")) // Oranye
                "Darurat" -> txtKategoriPost.setTextColor(Color.parseColor("#F44336")) // Merah
                else -> txtKategoriPost.setTextColor(Color.GRAY)
            }

            // Logika Menampilkan Gambar Base64 (Tetap)
            if (!post.imageUrl.isNullOrEmpty()) {
                try {
                    val imageBytes = Base64.decode(post.imageUrl, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    imgPost.setImageBitmap(decodedImage)
                    imgPost.visibility = View.VISIBLE
                } catch (e: Exception) {
                    imgPost.setImageResource(android.R.drawable.ic_menu_report_image)
                }
            } else {
                imgPost.visibility = View.GONE
            }
        }
    }
}