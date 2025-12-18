package com.example.citypulse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.citypulse.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var postsAdapter: PostAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Inisialisasi RecyclerView dan Adapter
        recyclerView = findViewById(R.id.recyclerViewPosts)
        postsAdapter = PostAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postsAdapter

        // Mendapatkan data pengguna dari Firebase
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val database = FirebaseDatabase.getInstance().getReference("users").child(userId)

            // Ambil data UserModel dari Firebase
            database.get().addOnSuccessListener { dataSnapshot ->
                val user = dataSnapshot.getValue(UserModel::class.java)
                if (user != null) {
                    // Menampilkan data user di Profile
                    binding.txtName.text = user.nama
                    binding.txtUsername.text = user.email
                    binding.txtLocation.text = "Lokasi Pengguna" // Sesuaikan lokasi jika ada
                }
            }
        }

        // Ambil data postingan dari Firebase
        val postDatabase = FirebaseDatabase.getInstance().getReference("posts")
        postDatabase.get().addOnSuccessListener { dataSnapshot ->
            val posts = mutableListOf<PostData>()
            for (snapshot in dataSnapshot.children) {
                val post = snapshot.getValue(PostData::class.java)
                if (post != null) {
                    posts.add(post)
                }
            }
            postsAdapter.submitList(posts) // Tampilkan postingan di RecyclerView
        }
    }
}
