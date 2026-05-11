package com.example.bilanganku.data.model

import com.google.gson.annotations.SerializedName

data class SistemBilangan(
    @SerializedName("nama") val nama: String,
    @SerializedName("basis") val basis: Int,
    @SerializedName("deskripsi") val deskripsi: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("warnaHex") val warnaHex: String
)