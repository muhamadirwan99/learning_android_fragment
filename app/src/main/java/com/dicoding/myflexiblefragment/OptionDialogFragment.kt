package com.dicoding.myflexiblefragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment


// DialogFragment adalah fragment khusus untuk menampilkan dialog
// Digunakan untuk interaksi yang membutuhkan pilihan dari user
class OptionDialogFragment : DialogFragment() {
    // Deklarasi widget untuk radio button dan button di dialog
    private lateinit var btnChoose: Button
    private lateinit var btnClose: Button
    private lateinit var rgOptions: RadioGroup
    private lateinit var rbSaf: RadioButton
    private lateinit var rbMou: RadioButton
    private lateinit var rbLvg: RadioButton
    private lateinit var rbMoyes: RadioButton

    // Listener untuk komunikasi balik ke fragment parent
    // Nullable karena belum tentu ada yang mendengarkan
    private var optionDialogListener: OnOptionDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_option_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi semua widget dari layout
        btnChoose = view.findViewById(R.id.btn_choose)
        btnClose = view.findViewById(R.id.btn_close)
        rgOptions = view.findViewById(R.id.rg_options)
        rbSaf = view.findViewById(R.id.rb_saf)
        rbLvg = view.findViewById(R.id.rb_lvg)
        rbMou = view.findViewById(R.id.rb_mou)
        rbMoyes = view.findViewById(R.id.rb_moyes)

        // Handler untuk button "Choose"
        btnChoose.setOnClickListener {
            // Mengambil ID dari radio button yang dipilih
            val checkedRadioButtonId = rgOptions.checkedRadioButtonId
            // Memastikan ada radio button yang dipilih (-1 artinya tidak ada yang dipilih)
            if (checkedRadioButtonId != -1){
                // when adalah switch-case di Kotlin untuk memilih berdasarkan kondisi
                // Mengambil text dari radio button yang dipilih
                var coach: String? = when(checkedRadioButtonId) {
                    R.id.rb_saf -> rbSaf.text.toString().trim()
                    R.id.rb_mou -> rbMou.text.toString().trim()
                    R.id.rb_lvg -> rbLvg.text.toString().trim()
                    R.id.rb_moyes -> rbMoyes.text.toString().trim()
                    else -> null
                }

                // Memanggil callback ke parent fragment dengan pilihan user
                // ?. adalah safe call operator, hanya dipanggil jika listener tidak null
                optionDialogListener?.onOptionChosen(coach)
                // Menutup dialog setelah pilihan dibuat
                dialog?.dismiss()
            }
        }

        // Handler untuk button "Close"
        btnClose.setOnClickListener {
            // cancel() menutup dialog tanpa melakukan aksi apapun
            dialog?.cancel()
        }
    }

    // onAttach dipanggil saat fragment di-attach ke parent
    // Digunakan untuk setup listener agar bisa berkomunikasi dengan parent
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Mendapatkan reference ke parent fragment
        val fragment = parentFragment

        // Mengecek apakah parent adalah DetailCategoryFragment
        // Jika ya, ambil listener dari parent untuk callback nanti
        if (fragment is DetailCategoryFragment){
            this.optionDialogListener = fragment.optionDialogListener
        }
    }

    // onDetach dipanggil saat fragment dilepas dari parent
    // Penting untuk cleanup dan mencegah memory leak
    override fun onDetach() {
        super.onDetach()
        // Set listener ke null untuk memutus referensi
        this.optionDialogListener = null
    }

    // Interface untuk callback ke parent fragment
    // Mendefinisikan kontrak komunikasi antara dialog dan parent
    interface OnOptionDialogListener {
        // Method yang akan dipanggil saat user memilih opsi
        fun onOptionChosen(text: String?)
    }
}