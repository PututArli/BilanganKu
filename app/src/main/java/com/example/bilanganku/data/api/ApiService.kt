package com.example.bilanganku.data.api

import com.example.bilanganku.data.model.SistemBilangan
import retrofit2.http.GET

interface ApiService {
    @GET("https://gist.githubusercontent.com/PututArli/121a08c7f5294ecd5251f828821a5573/raw/8a16d42d67b5876f6c0e15b0d205401e868668c2/bilanganku.json")
    suspend fun getSistemBilangan(): List<SistemBilangan>
}