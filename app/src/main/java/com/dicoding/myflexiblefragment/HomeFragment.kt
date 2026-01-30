package com.dicoding.myflexiblefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.commit


// Fragment ini adalah halaman utama aplikasi
// Implements View.OnClickListener untuk menangani klik tombol secara terpusat
class HomeFragment : Fragment(), View.OnClickListener {

    // onCreateView dipanggil untuk membuat tampilan fragment
    // Mirip dengan build() method di Flutter widget
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Mengubah layout XML menjadi View object yang bisa ditampilkan
        // Inflater seperti "builder" yang membaca XML dan membuat UI dari sana
        // Parameter false artinya jangan langsung attach ke parent, biar sistem yang handle
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    // onViewCreated dipanggil setelah view sudah dibuat
    // Di sini kita setup listener, inisialisasi widget, dll
    // Mirip dengan initState() di Flutter StatefulWidget
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mencari dan mendapatkan referensi ke button dari layout
        // Mirip dengan key di Flutter atau mencari widget by id
        val btnCategory: Button = view.findViewById<Button>(R.id.btn_category)
        // Mendaftarkan 'this' sebagai click listener
        // Saat button diklik, method onClick() akan dipanggil
        btnCategory.setOnClickListener(this)
    }

    // Method ini dipanggil otomatis saat view yang terdaftar sebagai listener diklik
    // Mirip dengan onPressed di Flutter Button
    override fun onClick(v: View?) {
        // Mengecek id dari view yang diklik untuk memastikan tombol yang benar
        if (v?.id == R.id.btn_category){
            // Membuat instance fragment baru yang akan ditampilkan
            val categoryFragment = CategoryFragment()
            // Mendapatkan FragmentManager dari parent untuk melakukan navigasi
            // parentFragmentManager digunakan untuk navigasi antar fragment di level yang sama
            val fragmentManager = parentFragmentManager

            // Melakukan transaction untuk mengganti fragment
            fragmentManager.commit {
                // Menambahkan transaction ke back stack agar bisa back dengan tombol back
                // Mirip dengan Navigator.push() di Flutter yang bisa di-pop
                addToBackStack(null)
                // Mengganti fragment di container dengan CategoryFragment
                // replace() akan menghapus fragment lama dan menampilkan yang baru
                // Tag digunakan untuk identifikasi fragment jika perlu dicari nanti
                replace(R.id.frame_container, categoryFragment, CategoryFragment::class.java.simpleName)
            }
        }
    }


}




