package com.example.bilanganku.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bilanganku.ui.component.BilanganListItem
import com.example.bilanganku.ui.navigation.Screen
import com.example.bilanganku.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarBilanganScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val inputValue = viewModel.inputValue
    val inputBase = viewModel.inputBase
    val dataList = viewModel.dataList
    val isLoading = viewModel.isLoading
    val isError = viewModel.isError

    val validChars = remember(inputBase) {
        when (inputBase) {
            2 -> listOf('0', '1')
            8 -> ('0'..'7').toList()
            10 -> ('0'..'9').toList()
            16 -> ('0'..'9').toList() + ('A'..'F').toList() + ('a'..'f').toList()
            else -> emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Konversi Basis", fontWeight = FontWeight.Bold) },
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
        containerColor = Color(0xFFF1F5F9)
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF4F46E5))
            }
        } else if (isError) {
            Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Koneksi Terputus",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Silakan periksa jaringan internet Anda.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadData() }) {
                        Text("Coba Lagi")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                        Text(
                            text = "Pilih Basis Input",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(dataList) { sistem ->
                                val isSelected = inputBase == sistem.basis
                                val cardBg = if (isSelected) Color(0xFF4F46E5) else Color.White
                                val textColor = if (isSelected) Color.White else Color(0xFF334155)

                                Surface(
                                    onClick = {
                                        viewModel.onInputBaseChange(sistem.basis)
                                        viewModel.onInputValueChange("")
                                    },
                                    shape = RoundedCornerShape(16.dp),
                                    color = cardBg,
                                    shadowElevation = if (isSelected) 8.dp else 2.dp
                                ) {
                                    Text(
                                        text = sistem.nama,
                                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(28.dp))
                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { newValue ->
                                if (newValue.all { it in validChars }) {
                                    viewModel.onInputValueChange(newValue.uppercase())
                                }
                            },
                            placeholder = { Text("Ketik angka di sini...", color = Color(0xFF94A3B8)) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = if (inputBase == 16) KeyboardType.Text else KeyboardType.Number
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = Color(0xFF4F46E5),
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            ),
                            trailingIcon = {
                                if (inputValue.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onInputValueChange("") }) {
                                        Icon(Icons.Default.Clear, contentDescription = null, tint = Color(0xFF94A3B8))
                                    }
                                }
                            },
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = "Hasil Konversi",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                items(dataList) { data ->
                    if (data.basis != inputBase) {
                        BilanganListItem(data, inputValue, inputBase) {
                            navController.navigate(Screen.Detail.createRoute(data.nama))
                        }
                    }
                }
            }
        }
    }
}
