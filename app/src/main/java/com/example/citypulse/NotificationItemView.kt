package com.example.citypulse

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView

// Gunakan FrameLayout sebagai parent agar cocok dengan CardView di XML
class NotificationItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // Gunakan lazy atau inisialisasi di init
    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var tvTime: TextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_notificationitemview, this, true)
        tvTitle = view.findViewById(R.id.tvTitle)
        tvMessage = view.findViewById(R.id.tvMessage)
        tvTime = view.findViewById(R.id.tvTime)
    }

    fun bind(title: String?, message: String?, time: String) {
        // Tambahkan pengecekan null/kosong untuk memastikan teks terlihat
        tvTitle.text = if (title.isNullOrEmpty()) "Kategori Kosong" else title
        tvMessage.text = if (message.isNullOrEmpty()) "Status Kosong" else message
        tvTime.text = time
    }

}