package com.example.bilanganku.model

import androidx.annotation.DrawableRes

data class SistemBilangan(
    val nama: String,
    val deskripsi: String,
    val basis: Int,
    @get:DrawableRes val imageRes: Int
)