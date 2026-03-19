package com.example.bilanganku.model

import com.example.bilanganku.R

object SistemBilanganSource {
    val dummyData = listOf(
        SistemBilangan("Desimal", "Sistem berbasis 10 (0-9).", 10, R.drawable.ic_desimal),
        SistemBilangan("Biner", "Sistem berbasis 2 (0-1).", 2, R.drawable.ic_biner),
        SistemBilangan("Oktal", "Sistem berbasis 8 (0-7).", 8, R.drawable.ic_oktal),
        SistemBilangan("Hexadesimal", "Sistem berbasis 16 (0-F).", 16, R.drawable.ic_hex),
        SistemBilangan("BCD", "Binary Coded Decimal.", 2, R.drawable.ic_biner)
    )
}