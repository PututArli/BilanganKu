package com.example.bilanganku

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bilanganku.model.SistemBilangan
import com.example.bilanganku.model.SistemBilanganSource
import com.example.bilanganku.ui.theme.BilanganKuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BilanganKuTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    DaftarBilanganScreen()
                }
            }
        }
    }
}

@Composable
fun DaftarBilanganScreen() {
    var inputValue by remember { mutableStateOf("") }
    var inputBase by remember { mutableIntStateOf(10) }

    val validChars = remember(inputBase) {
        when (inputBase) {
            2 -> listOf('0', '1')
            8 -> ('0'..'7').toList()
            10 -> ('0'..'9').toList()
            16 -> ('0'..'9').toList() + ('A'..'F').toList() + ('a'..'f').toList()
            else -> emptyList()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "BilanganKu",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Pilih Basis Input",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(SistemBilanganSource.dummyData) { sistem ->
                        val isSelected = inputBase == sistem.basis
                        Surface(
                            onClick = {
                                inputBase = sistem.basis
                                inputValue = ""
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            Text(
                                text = sistem.nama,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { newValue ->
                        if (newValue.all { it in validChars }) {
                            inputValue = newValue.uppercase()
                        }
                    },
                    label = { Text("Input Angka") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (inputBase == 16) KeyboardType.Text else KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        if (inputValue.isNotEmpty()) {
                            IconButton(onClick = { inputValue = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Sistem Utama",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(SistemBilanganSource.dummyData) { data ->
                        BilanganRowItem(sistem = data, input = inputValue, currentBase = inputBase)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Daftar Lengkap",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        items(SistemBilanganSource.dummyData) { data ->
            DetailBilanganItem(sistem = data, input = inputValue, currentBase = inputBase)
        }
    }
}

fun convertUniversal(input: String, currentBase: Int, targetBase: Int): String {
    if (input.isEmpty()) return "0"
    return try {
        val decimalValue = input.toLong(currentBase)
        decimalValue.toString(targetBase).uppercase()
    } catch (e: Exception) {
        "ERROR"
    }
}

@Composable
fun BilanganRowItem(sistem: SistemBilangan, input: String, currentBase: Int) {
    val context = LocalContext.current
    val hasil = remember(input, currentBase) { convertUniversal(input, currentBase, sistem.basis) }

    Card(
        modifier = Modifier.width(150.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = sistem.warnaBg),
        onClick = { Toast.makeText(context, "Sistem ${sistem.nama} dipilih", Toast.LENGTH_SHORT).show() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier.size(42.dp).background(Color.White.copy(0.5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(painter = painterResource(id = sistem.imageRes), contentDescription = null, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = sistem.nama, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = hasil, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun DetailBilanganItem(sistem: SistemBilangan, input: String, currentBase: Int) {
    val context = LocalContext.current
    val hasil = remember(input, currentBase) { convertUniversal(input, currentBase, sistem.basis) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = sistem.warnaBg.copy(0.3f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(52.dp).background(Color.White, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(painter = painterResource(id = sistem.imageRes), contentDescription = null, modifier = Modifier.size(30.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = sistem.nama, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = hasil, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
            }
            Button(
                onClick = { Toast.makeText(context, "Hasil $hasil disalin", Toast.LENGTH_SHORT).show() },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Salin", fontSize = 10.sp, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DaftarBilanganPreview() {
    BilanganKuTheme {
        DaftarBilanganScreen()
    }
}