package com.dicoding.myflexiblefragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


// Fragment untuk menampilkan detail dari kategori
class DetailCategoryFragment : Fragment() {

    // Deklarasi variable untuk widget UI
    // lateinit artinya akan diinisialisasi nanti, bukan saat object dibuat
    // Digunakan karena widget baru bisa diakses setelah view di-inflate
    private lateinit var tvCategoryName: TextView
    private lateinit var tvCategoryDescription: TextView
    private lateinit var btnProfile: Button
    private lateinit var btnShowDialog: Button

    // Variable untuk menyimpan description
    // Nullable karena bisa saja tidak ada data yang dikirim
    var description: String? = null

    // companion object adalah static member di Kotlin
    // Mirip dengan static variable di Java atau constant di Flutter
    // Digunakan untuk menyimpan konstanta yang sama untuk semua instance
    companion object {
        // Key untuk mengambil data dari Bundle
        // Menggunakan konstanta mencegah typo saat passing/receiving data
        val EXTRA_NAME = "extra_name"
        val EXTRA_DESCRIPTION = "extra_description"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menginisialisasi semua widget dengan mencari berdasarkan ID dari layout
        tvCategoryName = view.findViewById<TextView>(R.id.tv_category_name)
        tvCategoryDescription = view.findViewById<TextView>(R.id.tv_category_description)
        btnProfile = view.findViewById<Button>(R.id.btn_profile)
        btnShowDialog = view.findViewById<Button>(R.id.btn_show_dialog)

        // Mengecek apakah ada savedInstanceState (terjadi ketika configuration change seperti rotasi)
        // Ini untuk restore data yang tersimpan sebelumnya
        if (savedInstanceState != null){
            val descFromBundle = savedInstanceState.getString(EXTRA_DESCRIPTION)
            description = descFromBundle
        }

        // Mengecek apakah ada arguments yang dikirim dari fragment sebelumnya
        // arguments adalah cara standar passing data antar fragment
        if (arguments != null){
            // Mengambil data dari Bundle
            val categoryName = arguments?.getString(EXTRA_NAME)
            // Menampilkan data ke widget TextView
            tvCategoryName.text = categoryName
            tvCategoryDescription.text = description
        }

        // Menangani klik button untuk menampilkan dialog
        btnShowDialog.setOnClickListener {
            // Membuat instance dialog fragment
            val optionDialogFragment = OptionDialogFragment()

            // Menggunakan childFragmentManager karena dialog adalah child dari fragment ini
            // childFragmentManager: untuk fragment di dalam fragment (parent-child relationship)
            // parentFragmentManager: untuk fragment di level yang sama (sibling)
            val fragmentManager = childFragmentManager
            // show() adalah method khusus DialogFragment untuk menampilkan dialog
            optionDialogFragment.show(fragmentManager, OptionDialogFragment::class.java.simpleName)
        }

        // Menangani klik button untuk navigasi ke Activity lain
        btnProfile.setOnClickListener {
            // Membuat Intent untuk pindah ke Activity lain
            // Intent seperti route/navigation di Flutter
            // requireActivity() mendapatkan Activity yang menempatkan fragment ini
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            // startActivity menjalankan navigasi
            startActivity((intent))
        }
    }

    // Mendefinisikan listener untuk menerima callback dari OptionDialogFragment
    // internal artinya hanya bisa diakses dari module yang sama
    // object: membuat anonymous class yang mengimplementasikan interface
    // Ini pattern untuk komunikasi antar fragment (callback)
    internal var optionDialogListener: OptionDialogFragment.OnOptionDialogListener = object : OptionDialogFragment.OnOptionDialogListener {
        // Method ini akan dipanggil dari OptionDialogFragment saat user memilih opsi
        override fun onOptionChosen(text: String?) {
            // Menampilkan Toast (notifikasi singkat) dengan pilihan user
            Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
        }
    }

}