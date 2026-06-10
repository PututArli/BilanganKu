package com.example.bilanganku.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bilanganku.data.model.SistemBilangan
import com.example.bilanganku.ui.viewmodel.MainViewModel
import com.example.bilanganku.util.ConversionUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBilanganScreen(
    sistem: SistemBilangan,
    navController: NavController,
    viewModel: MainViewModel,
) {
    val input = viewModel.inputValue
    val currentBase = viewModel.inputBase
    val hasil = remember(input, currentBase) {
        ConversionUtils.convertUniversal(input, currentBase, sistem.basis)
    }
    val cardColor = remember(sistem.warnaHex) {
        Color(sistem.warnaHex.toColorInt())
    }
    var isSaving by remember { mutableStateOf(value = false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(sistem.nama, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF1F5F9),
                    titleContentColor = Color(0xFF0F172A)
                )
            )
        },
        containerColor = Color(0xFFF1F5F9),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(cardColor.copy(alpha = 0.15f), RoundedCornerShape(32.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = sistem.imageUrl,
                    contentDescription = sistem.nama,
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Hasil Konversi", fontSize = 16.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = hasil, fontSize = 48.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Dari Basis $currentBase ke ${sistem.basis}", fontSize = 16.sp, color = Color(0xFF475569), fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = sistem.deskripsi, fontSize = 15.sp, color = Color(0xFF64748B), lineHeight = 24.sp)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (input.isNotEmpty()) {
                        coroutineScope.launch {
                            isSaving = true
                            delay(1500.milliseconds)
                            viewModel.saveHistory(hasil, sistem.basis, sistem.nama)
                            isSaving = false
                            snackbarHostState.showSnackbar("Disimpan ke riwayat!")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text("Simpan Riwayat", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
