package com.example.bilanganku.model

import com.example.bilanganku.R

object SistemBilanganSource {
    val dummyData = listOf(
        SistemBilangan("Desimal", "Sistem bilangan berbasis sepuluh (0-9).", 10, R.drawable.ic_desimal),
        SistemBilangan("Biner", "Sistem berbasis dua untuk sirkuit digital (0-1).", 2, R.drawable.ic_biner),
        SistemBilangan("Oktal", "Sistem bilangan berbasis delapan (0-7).", 8, R.drawable.ic_oktal),
        SistemBilangan("Hexadesimal", "Sistem berbasis enam belas (0-9 dan A-F).", 16, R.drawable.ic_hex),
        SistemBilangan("Biner Terkode Desimal", "Sistem BCD yang merepresentasikan angka desimal.", 2, R.drawable.ic_biner)
    )
}