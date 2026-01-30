package com.dicoding.myflexiblefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


// Fragment untuk menampilkan halaman kategori
// Implements View.OnClickListener untuk handle click event
class CategoryFragment : Fragment(), View.OnClickListener {
    // Membuat tampilan fragment dari layout XML
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    // Setup setelah view berhasil dibuat
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mencari button di layout dan set listener untuk menangani klik
        val btnDetailCategory: Button = view.findViewById<Button>(R.id.detail_category)
        btnDetailCategory.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.detail_category){
            // Membuat instance fragment detail
            val detailCategoryFragment = DetailCategoryFragment()

            // Membuat Bundle untuk mengirim data ke fragment tujuan
            // Bundle seperti Map<String, Any> untuk passing data antar screen
            // Mirip dengan arguments di Navigator.push() di Flutter
            val bundle = Bundle()
            bundle.putString(DetailCategoryFragment.EXTRA_NAME, "Lifestyle")
            val description = "Kategori ini akan berisi produk-produk lifestyle"

            // Memasukkan bundle ke dalam arguments fragment
            // Ini cara standar passing data antar fragment
            detailCategoryFragment.arguments = bundle
            // Cara alternatif: set property langsung (tidak recommended untuk data penting)
            // Karena property langsung tidak akan tersimpan saat configuration change
            detailCategoryFragment.description = description

            // Mendapatkan FragmentManager untuk navigasi
            val fragmentManager = parentFragmentManager
            // beginTransaction() memulai serangkaian operasi fragment
            // apply{} adalah scope function untuk menulis kode lebih ringkas
            fragmentManager.beginTransaction().apply {
                // Mengganti fragment yang ada dengan DetailCategoryFragment
                replace(R.id.frame_container, detailCategoryFragment, DetailCategoryFragment::class.java.simpleName)
                // Menambahkan ke back stack agar bisa kembali dengan tombol back
                addToBackStack(null)
                // commit() menjalankan transaction yang sudah didefinisikan
                // Tanpa commit(), perubahan tidak akan terjadi
                commit()
            }

        }
    }

}