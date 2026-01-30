# MyFlexibleFragment - Aplikasi Android Native

Proyek pembelajaran Android Native untuk memahami konsep Fragment, Navigation, dan komunikasi antar komponen.

## 📖 Deskripsi Proyek

Aplikasi ini mendemonstrasikan penggunaan **Fragment** dalam Android Native, termasuk:
- Navigasi antar Fragment
- Passing data antar Fragment
- Dialog Fragment
- Komunikasi Fragment dengan Activity
- Fragment Manager dan Back Stack

## 🎯 Konsep Utama untuk Flutter Developer

### 1. **Fragment vs Widget**
- **Fragment** di Android ≈ **Widget/Screen** di Flutter
- Fragment adalah komponen UI yang bisa digunakan ulang dan memiliki lifecycle sendiri
- Bedanya: Fragment harus berada dalam Activity (seperti container)

### 2. **Activity vs Route**
- **Activity** di Android ≈ **Route/Screen** di Flutter (Navigator.push)
- Activity adalah layar penuh yang independen
- MainActivity berisi frame_container untuk menampung Fragment

### 3. **Fragment Lifecycle**
```
onAttach() → onCreate() → onCreateView() → onViewCreated() → onStart() → onResume()
                              ↓
                          onPause() → onStop() → onDestroyView() → onDestroy() → onDetach()
```

**Perbandingan dengan Flutter:**
- `onCreateView()` ≈ `build()` method - membuat tampilan
- `onViewCreated()` ≈ `initState()` - inisialisasi widget/listener
- `onDestroy()` ≈ `dispose()` - cleanup resources

### 4. **Navigation**
```kotlin
// Android - Fragment Navigation
fragmentManager.commit {
    replace(R.id.frame_container, MyFragment())
    addToBackStack(null)  // Bisa back dengan tombol back
}

// Flutter Equivalent
Navigator.push(
  context,
  MaterialPageRoute(builder: (context) => MyScreen()),
);
```

### 5. **Passing Data**
```kotlin
// Android - Menggunakan Bundle
val bundle = Bundle()
bundle.putString("key", "value")
fragment.arguments = bundle

// Flutter Equivalent
Navigator.push(
  context,
  MaterialPageRoute(
    builder: (context) => MyScreen(title: "value"),
  ),
);
```

### 6. **Fragment Manager**
- **parentFragmentManager**: Navigasi antar Fragment di level yang sama (siblings)
- **childFragmentManager**: Fragment dalam Fragment (parent-child)
- **supportFragmentManager**: Fragment dalam Activity

**Analogi Flutter:**
- parentFragmentManager ≈ Navigator.of(context)
- childFragmentManager ≈ nested Navigator

## 🏗️ Struktur Aplikasi

```
MainActivity (Activity)
    └── frame_container (FrameLayout)
         ├── HomeFragment
         ├── CategoryFragment
         └── DetailCategoryFragment
              └── OptionDialogFragment (child)

ProfileActivity (Activity terpisah)
```

### Flow Navigasi:
1. **HomeFragment** → Tombol "Category" → **CategoryFragment**
2. **CategoryFragment** → Tombol "Lifestyle" → **DetailCategoryFragment**
3. **DetailCategoryFragment** → Tombol "Show Dialog" → **OptionDialogFragment**
4. **DetailCategoryFragment** → Tombol "Profile" → **ProfileActivity**

## 📁 File-file Penting

### 1. **MainActivity.kt**
- Entry point aplikasi
- Setup HomeFragment sebagai fragment pertama
- Mengecek fragment duplikat saat configuration change (rotasi)

**Poin Penting:**
```kotlin
// Cek fragment sudah ada atau belum (mencegah duplikasi saat rotasi)
val fragment = fragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)
if (fragment !is HomeFragment) {
    // Tambahkan fragment baru
}
```

### 2. **HomeFragment.kt**
- Fragment halaman utama
- Implements `View.OnClickListener` untuk handle click event
- Navigasi ke CategoryFragment menggunakan `replace()`

**Konsep Penting:**
- `inflater.inflate()`: Mengubah XML layout → View object
- `findViewById()`: Mencari widget berdasarkan ID
- `fragmentManager.commit {}`: Transaction untuk mengganti fragment

### 3. **CategoryFragment.kt**
- Menampilkan kategori
- Passing data ke DetailCategoryFragment menggunakan Bundle

**Cara Passing Data:**
```kotlin
// 1. Via Bundle (RECOMMENDED - survive configuration change)
val bundle = Bundle()
bundle.putString(EXTRA_NAME, "Lifestyle")
fragment.arguments = bundle

// 2. Via Property Langsung (NOT RECOMMENDED)
fragment.description = "Data hilang saat rotasi"
```

### 4. **DetailCategoryFragment.kt**
- Menerima data dari CategoryFragment
- Menampilkan OptionDialogFragment
- Navigasi ke ProfileActivity

**Konsep Penting:**
- `lateinit var`: Variable diinisialisasi nanti (setelah view inflate)
- `companion object`: Static member (konstanta)
- `savedInstanceState`: Restore data saat configuration change
- `childFragmentManager`: Untuk dialog fragment (parent-child)

### 5. **OptionDialogFragment.kt**
- Dialog khusus untuk pilihan user
- Menggunakan interface untuk callback ke parent
- Lifecycle: onAttach → onDetach

**Pattern Komunikasi:**
```kotlin
// 1. Define interface di DialogFragment
interface OnOptionDialogListener {
    fun onOptionChosen(text: String?)
}

// 2. Parent implement listener
internal var optionDialogListener = object : OnOptionDialogListener {
    override fun onOptionChosen(text: String?) {
        // Handle result
    }
}

// 3. Dialog connect ke parent di onAttach()
override fun onAttach(context: Context) {
    val fragment = parentFragment
    if (fragment is DetailCategoryFragment) {
        this.optionDialogListener = fragment.optionDialogListener
    }
}

// 4. Cleanup di onDetach()
override fun onDetach() {
    this.optionDialogListener = null  // Mencegah memory leak
}
```

### 6. **ProfileActivity.kt**
- Activity terpisah untuk profile
- Navigasi menggunakan Intent

## 🔑 Konsep Penting

### 1. **Fragment Transaction**
```kotlin
// Cara 1: Menggunakan commit extension
fragmentManager.commit {
    replace(R.id.container, fragment)
    addToBackStack(null)
}

// Cara 2: Manual transaction
fragmentManager.beginTransaction().apply {
    replace(R.id.container, fragment)
    addToBackStack(null)
    commit()
}
```

### 2. **Back Stack**
- `addToBackStack(null)`: Fragment bisa di-back dengan tombol back
- Tanpa back stack: Fragment langsung hilang, tidak bisa kembali
- `null` adalah tag (bisa diisi nama untuk identifikasi)

### 3. **findViewById vs View Binding**
Proyek ini menggunakan `findViewById()` untuk pembelajaran.
Alternatif modern: **View Binding** (lebih aman, type-safe)

### 4. **Nullable & Safe Call**
```kotlin
var description: String?  // Nullable
description?.length       // Safe call - cek null dulu
```

### 5. **lateinit vs lazy**
```kotlin
lateinit var button: Button  // Inisialisasi nanti, pasti ada
val data: String by lazy { getData() }  // Inisialisasi saat pertama diakses
```

## 🎨 Layout Files

### activity_main.xml
- **FrameLayout** sebagai container untuk Fragment
- `match_parent`: Ukuran penuh layar

### fragment_home.xml, fragment_category.xml, dll
- **LinearLayout**: Susun widget secara vertikal/horizontal
- `android:id`: Identifikasi widget untuk findViewById()
- `@string/...`: Reference ke strings.xml (best practice)

## 🔄 Perbandingan dengan Flutter

| Android Native | Flutter | Penjelasan |
|---------------|---------|------------|
| Fragment | Widget/Screen | Komponen UI |
| Activity | Route/MaterialApp | Layar penuh |
| onCreate() | initState() | Inisialisasi |
| onDestroy() | dispose() | Cleanup |
| FragmentManager | Navigator | Navigasi |
| Bundle | Constructor params | Passing data |
| Intent | Navigator.push | Pindah screen |
| Toast | SnackBar | Notifikasi singkat |
| findViewById() | Key/GlobalKey | Akses widget |
| ViewGroup | Container/Column/Row | Layout parent |

## 📱 Fitur Aplikasi

1. ✅ Navigasi multi-fragment
2. ✅ Passing data antar fragment
3. ✅ Dialog fragment dengan callback
4. ✅ Navigasi ke activity lain
5. ✅ Handle configuration change (rotasi)
6. ✅ Back stack management

## 🚀 Cara Menjalankan

1. Buka proyek di Android Studio
2. Sync Gradle
3. Pilih emulator atau device
4. Run aplikasi (Shift + F10)

## 📚 Pelajaran yang Didapat

### Untuk Flutter Developer:
1. **XML Layout vs Widget Tree**: Android pisahkan layout (XML) dan logic (Kotlin)
2. **Lifecycle Manual**: Harus manual handle lifecycle, tidak otomatis seperti Flutter
3. **findViewById**: Harus manual cari widget, tidak ada state management otomatis
4. **Fragment Manager**: Navigasi lebih verbose dibanding Flutter Navigator
5. **Bundle**: Passing data lebih manual, harus serialize/deserialize

### Kelebihan Android Native:
- Kontrol penuh atas lifecycle
- Performa native
- Akses penuh Android API

### Kelebihan Flutter:
- Kode lebih ringkas
- Hot reload lebih cepat
- Cross-platform (Android + iOS)
- Widget tree lebih intuitif

## 🐛 Perhatian Penting

### 1. **Memory Leak**
Selalu cleanup listener di `onDetach()` atau `onDestroy()`:
```kotlin
override fun onDetach() {
    super.onDetach()
    this.listener = null  // PENTING!
}
```

### 2. **Configuration Change**
Gunakan `Bundle` untuk passing data, bukan property langsung:
```kotlin
// ✅ BENAR - survive rotation
fragment.arguments = bundle

// ❌ SALAH - hilang saat rotation
fragment.property = data
```

### 3. **Fragment Manager**
- `parentFragmentManager`: Sibling navigation
- `childFragmentManager`: Parent-child navigation
- `supportFragmentManager`: Activity → Fragment

### 4. **lateinit Crash**
Pastikan inisialisasi sebelum digunakan:
```kotlin
lateinit var button: Button

// Harus init dulu di onViewCreated()
button = view.findViewById(R.id.button)
```

## 📖 Referensi

- [Android Fragments Guide](https://developer.android.com/guide/fragments)
- [Fragment Lifecycle](https://developer.android.com/guide/fragments/lifecycle)
- [Fragment Transactions](https://developer.android.com/guide/fragments/transactions)

## 👨‍💻 Catatan Pembelajaran

Proyek ini dibuat untuk belajar Android Native dengan background Flutter developer.
Semua komentar di kode ditulis untuk memudahkan pemahaman dan sebagai referensi di masa depan.

---

**Happy Coding! 🎉**

*Catatan: Kode ini untuk pembelajaran, production code sebaiknya gunakan:*
- View Binding / Data Binding
- ViewModel & LiveData
- Navigation Component
- Dependency Injection (Hilt/Koin)
