package com.example.bilanganku.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class SistemBilangan(
    val nama: String,
    val deskripsi: String,
    val basis: Int,
    @get:DrawableRes val imageRes: Int,
    val warnaBg: Color
)