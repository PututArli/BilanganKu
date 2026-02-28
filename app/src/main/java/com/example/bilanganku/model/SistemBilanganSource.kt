package com.example.bilanganku.model

import com.example.bilanganku.R

object SistemBilanganSource {
    val dummyData = listOf(
        SistemBilangan("Desimal", 10, "Basis 10", R.drawable.ic_desimal),
        SistemBilangan("Biner", 2, "Basis 2", R.drawable.ic_biner),
        SistemBilangan("Oktal", 8, "Basis 8", R.drawable.ic_oktal),
        SistemBilangan("Hexadesimal", 16, "Basis 16", R.drawable.ic_hex)
    )
}