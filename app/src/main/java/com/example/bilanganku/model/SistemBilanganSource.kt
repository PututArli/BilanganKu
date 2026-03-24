package com.example.bilanganku.model

import com.example.bilanganku.R

object SistemBilanganSource {
    val dummyData = listOf(
        SistemBilangan("Desimal", "Sistem bilangan berbasis 10.", 10, R.drawable.ic_desimal),
        SistemBilangan("Biner", "Sistem bilangan berbasis 2.", 2, R.drawable.ic_biner),
        SistemBilangan("Oktal", "Sistem bilangan berbasis 8.", 8, R.drawable.ic_oktal),
        SistemBilangan("Hexadesimal", "Sistem bilangan berbasis 16.", 16, R.drawable.ic_hex)
    )
}