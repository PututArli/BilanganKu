package com.example.bilanganku.model

import androidx.compose.ui.graphics.Color

data class SistemBilangan(
    val nama: String,
    val basis: Int,
    val deskripsi: String,
    val imageUrl: String,
    val warnaBg: Color
)

object SistemBilanganSource {
    val dummyData = listOf(
        SistemBilangan("Desimal", 10, "Sistem bilangan berbasis 10.", "https://dummyimage.com/200x200/BBDEFB/000000&text=10", Color(0xFFBBDEFB)),
        SistemBilangan("Biner", 2, "Sistem bilangan berbasis 2.", "https://dummyimage.com/200x200/C8E6C9/000000&text=01", Color(0xFFC8E6C9)),
        SistemBilangan("Oktal", 8, "Sistem bilangan berbasis 8.", "https://dummyimage.com/200x200/FFE0B2/000000&text=8", Color(0xFFFFE0B2)),
        SistemBilangan("Hexadesimal", 16, "Sistem bilangan berbasis 16.", "https://dummyimage.com/200x200/E1BEE7/000000&text=16", Color(0xFFE1BEE7))
    )
}