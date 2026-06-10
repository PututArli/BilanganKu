package com.example.bilanganku.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bilanganku.ui.component.ResultCard
import com.example.bilanganku.util.ConversionUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PenerjemahTeksScreen(navController: NavController) {
    var textInput by remember { mutableStateOf("") }

    val binaryResult = ConversionUtils.textToBinary(textInput)
    val hexResult = ConversionUtils.textToHex(textInput)
    val decResult = ConversionUtils.textToDecimal(textInput)
    val octalResult = ConversionUtils.textToOctal(textInput)
    val base64Result = ConversionUtils.textToBase64(textInput)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sandi Teks", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("Ketik Teks / Password") },
                    placeholder = { Text("Contoh: Admin123") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF10B981),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    trailingIcon = {
                        if (textInput.isNotEmpty()) {
                            IconButton(onClick = { textInput = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item { ResultCard("Biner (ASCII)", binaryResult, Color(0xFF10B981)) }
            item { ResultCard("Hexadesimal", hexResult, Color(0xFF4F46E5)) }
            item { ResultCard("Desimal (ASCII)", decResult, Color(0xFFEC4899)) }
            item { ResultCard("Oktal", octalResult, Color(0xFFF59E0B)) }
            item { ResultCard("Base64 Encoding", base64Result, Color(0xFF06B6D4)) }
        }
    }
}
