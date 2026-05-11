package com.example.bilanganku.data.repository

import com.example.bilanganku.data.api.RetrofitClient
import com.example.bilanganku.data.model.SistemBilangan

class BilanganRepository {
    suspend fun getSistemBilangan(): List<SistemBilangan> {
        return try {
            RetrofitClient.instance.getSistemBilangan()
        } catch (e: Exception) {
            emptyList()
        }
    }
}