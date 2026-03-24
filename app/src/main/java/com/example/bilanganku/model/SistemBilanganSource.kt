package com.example.bilanganku.model

import androidx.compose.ui.graphics.Color
import com.example.bilanganku.R

object SistemBilanganSource {
    val dummyData = listOf(
        SistemBilangan("Desimal", "Sistem bilangan berbasis 10.", 10, R.drawable.ic_desimal, Color(0xFFD0E8FF)),
        SistemBilangan("Biner", "Sistem bilangan berbasis 2.", 2, R.drawable.ic_biner, Color(0xFFD1F2D3)),
        SistemBilangan("Oktal", "Sistem bilangan berbasis 8.", 8, R.drawable.ic_oktal, Color(0xFFFFE0B2)),
        SistemBilangan("Hexadesimal", "Sistem bilangan berbasis 16.", 16, R.drawable.ic_hex, Color(0xFFE1BEE7))
    )
}