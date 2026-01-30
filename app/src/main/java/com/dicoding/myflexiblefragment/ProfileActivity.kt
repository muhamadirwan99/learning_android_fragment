package com.dicoding.myflexiblefragment

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Activity untuk menampilkan halaman profile
// Activity adalah screen/layar penuh yang independen, berbeda dengan Fragment
class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengaktifkan mode edge-to-edge untuk tampilan modern
        enableEdgeToEdge()
        // Set layout untuk activity ini
        setContentView(R.layout.activity_profile)
        // Mengatur padding agar konten tidak tertimpa system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}