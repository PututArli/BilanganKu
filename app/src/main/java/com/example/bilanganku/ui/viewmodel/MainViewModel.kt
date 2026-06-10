package com.example.bilanganku.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bilanganku.data.model.SistemBilangan
import com.example.bilanganku.data.repository.BilanganRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BilanganRepository = BilanganRepository()) : ViewModel() {
    var inputValue by mutableStateOf("")
    var inputBase by mutableIntStateOf(10)
    var riwayatList by mutableStateOf(listOf<String>())
    var dataList by mutableStateOf<List<SistemBilangan>>(emptyList())
    var isLoading by mutableStateOf(true)
    var isError by mutableStateOf(false)

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            isLoading = true
            val result = repository.getSistemBilangan()
            if (result.isNotEmpty()) {
                dataList = result
                isError = false
            } else {
                isError = true
            }
            isLoading = false
        }
    }

    fun onInputValueChange(newValue: String) {
        inputValue = newValue
    }

    fun onInputBaseChange(newBase: Int) {
        inputBase = newBase
    }

    fun saveHistory(hasil: String, targetBasis: Int, targetNama: String) {
        viewModelScope.launch {
            val teksRiwayat = "$inputValue (Basis $inputBase) ➔ $hasil (Basis $targetBasis)"
            if (!riwayatList.contains(teksRiwayat)) {
                riwayatList = listOf(teksRiwayat) + riwayatList
            }
        }
    }
}
