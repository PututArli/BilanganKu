package com.example.bilanganku.model

import androidx.annotation.DrawableRes

data class SistemBilangan(
    val nama: String,
    val basis: Int,
    val deskripsi: String,
    @DrawableRes val imageRes: Int
)