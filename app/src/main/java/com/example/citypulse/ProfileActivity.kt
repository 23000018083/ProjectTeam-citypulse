package com.example.citypulse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.citypulse.databinding.ActivityProfileBinding
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var postsAdapter: PostAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Menggunakan View Binding agar sinkron dengan XML baru
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference

        // 2. Inisialisasi RecyclerView untuk menampilkan "Postingan Saya"
        setupRecyclerView()

        // 3. Ambil data User & Postingan berdasarkan SharedPreferences
        loadData()

        // 4. Inisialisasi Navigasi Footer
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {
        postsAdapter = PostAdapter()
        binding.recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            adapter = postsAdapter
        }
    }

    private fun loadData() {
        val sharedPrefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
        val userId = sharedPrefs.getString("CURRENT_USER_ID", "")
        val usernameShared = sharedPrefs.getString("USERNAME", "") // Gunakan ini untuk filter lebih aman

        if (!userId.isNullOrEmpty()) {
            // Ambil Data Profil
            database.child("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    if (user != null) {
                        binding.txtName.text = user.nama
                        binding.txtUsername.text = user.email

                        // JALANKAN pengambilan post DI SINI setelah nama user terisi
                        fetchMyPosts(user.nama)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun fetchMyPosts(namaUser: String) {
        database.child("posts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val myPosts = mutableListOf<PostData>()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(PostData::class.java)
                    // Filter menggunakan namaUser yang sudah pasti didapat dari Firebase
                    if (post != null && post.pembuat == namaUser) {
                        myPosts.add(post)
                    }
                }
                postsAdapter.submitList(myPosts.reversed())
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setupBottomNavigation() {
        // Navigasi ke Home (Maps)
        binding.navHome.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        // Navigasi ke Setting
        binding.navSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        // navProfile tidak perlu onClick karena sudah berada di halaman Profile
    }
}