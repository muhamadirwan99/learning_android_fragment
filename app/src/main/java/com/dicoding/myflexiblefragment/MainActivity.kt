package com.dicoding.myflexiblefragment

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengaktifkan tampilan edge-to-edge agar konten dapat menyentuh ujung layar (termasuk di bawah status bar dan navigation bar)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Mengatur padding agar konten tidak tertutup oleh system bars (status bar & navigation bar)
        // Ini penting untuk edge-to-edge layout agar UI tidak terpotong
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.frame_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mendapatkan FragmentManager untuk mengelola fragment di dalam activity
        val fragmentManager = supportFragmentManager
        val homeFragment = HomeFragment()

        // Mengecek apakah HomeFragment sudah ada di container atau belum berdasarkan tag
        // Ini mencegah fragment duplikat saat activity di-recreate (misal rotasi layar)
        val fragment = fragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)

        if (fragment !is HomeFragment){
            Log.d("MyFlexibleFragment", "Fragment Name :" + HomeFragment::class.java.simpleName)
            // Menambahkan HomeFragment ke dalam frame_container untuk pertama kali
            // add() digunakan karena ini fragment pertama, bukan replace
            fragmentManager.commit {
                add(R.id.frame_container, homeFragment, HomeFragment::class.java.simpleName)
            }
        }

    }
}