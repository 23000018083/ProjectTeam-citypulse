package com.example.citypulse

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class NotifActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notif)

        val notificationList: LinearLayout = findViewById(R.id.notificationList)
        val btnBack: ImageView = findViewById(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        // Query database dan urutkan berdasarkan postingan terbaru
        val postRef = FirebaseDatabase.getInstance().getReference("posts")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notificationList.removeAllViews()

                // Mengambil data dan membaliknya agar yang terbaru di atas
                val children = snapshot.children.reversed()

                for (data in children) {
                    val post = data.getValue(PostData::class.java) ?: continue

                    val itemView = NotificationItemView(this@NotifActivity)

                    // Bind data dari Firebase ke view item
                    itemView.bind(
                        title = "Kejadian: ${post.kategori}",
                        message = post.deskripsi,
                        time = post.pembuat // Atau gunakan timestamp jika sudah dikonversi
                    )

                    itemView.setOnClickListener {
                        val intent = Intent(this@NotifActivity, MapsActivity::class.java)
                        // Mengirim koordinat kejadian
                        intent.putExtra("EXTRA_LAT", post.latitude)
                        intent.putExtra("EXTRA_LNG", post.longitude)

                        // Penting: Agar tidak menumpuk banyak MapsActivity
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }

                    notificationList.addView(itemView)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}