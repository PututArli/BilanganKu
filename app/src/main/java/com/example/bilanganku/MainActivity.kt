package com.example.bilanganku

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                    val navController = rememberNavController()
                    AppNavigation(navController)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    var inputValue by remember { mutableStateOf("") }
    var inputBase by remember { mutableIntStateOf(10) }
    var riwayatList by remember { mutableStateOf(listOf<String>()) }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            DaftarBilanganScreen(
                navController = navController,
                inputValue = inputValue,
                onInputValueChange = { inputValue = it },
                inputBase = inputBase,
                onInputBaseChange = { inputBase = it }
            )
        }
        composable("detail/{nama}") { backStackEntry ->
            val nama = backStackEntry.arguments?.getString("nama")
            val sistem = SistemBilanganSource.dummyData.find { it.nama == nama }
            if (sistem != null) {
                DetailBilanganScreen(
                    sistem = sistem,
                    navController = navController,
                    input = inputValue,
                    currentBase = inputBase,
                    onSaveHistory = { hasil ->
                        val teksRiwayat = "$inputValue (Basis $inputBase) ➔ $hasil (Basis ${sistem.basis})"
                        if (!riwayatList.contains(teksRiwayat)) {
                            riwayatList = listOf(teksRiwayat) + riwayatList
                        }
                    }
                )
            }
        }
        composable("riwayat") {
            RiwayatScreen(navController, riwayatList)
        }
    }
}

@Composable
fun DaftarBilanganScreen(
    navController: NavController,
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    inputBase: Int,
    onInputBaseChange: (Int) -> Unit
) {
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("riwayat") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.List, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).statusBarsPadding(),
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
                                    onInputBaseChange(sistem.basis)
                                    onInputValueChange("")
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
                                onInputValueChange(newValue.uppercase())
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
                                IconButton(onClick = { onInputValueChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = null)
                                }
                            }
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Hasil Konversi",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            items(SistemBilanganSource.dummyData) { data ->
                if (data.basis != inputBase) {
                    BilanganListItem(data, inputValue, inputBase, navController)
                }
            }
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
fun BilanganListItem(sistem: SistemBilangan, input: String, currentBase: Int, navController: NavController) {
    val hasil = remember(input, currentBase) { convertUniversal(input, currentBase, sistem.basis) }
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp).clickable { navController.navigate("detail/${sistem.nama}") },
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBilanganScreen(
    sistem: SistemBilangan,
    navController: NavController,
    input: String,
    currentBase: Int,
    onSaveHistory: (String) -> Unit
) {
    val context = LocalContext.current
    val hasil = remember(input, currentBase) { convertUniversal(input, currentBase, sistem.basis) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Konversi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 24.dp, vertical = 8.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp).background(sistem.warnaBg, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(painter = painterResource(id = sistem.imageRes), contentDescription = null, modifier = Modifier.size(100.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = sistem.nama, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = sistem.deskripsi, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Basis Target: ${sistem.basis}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Hasil Konversi dari Basis $currentBase:", style = MaterialTheme.typography.titleMedium)
            Text(text = hasil, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (input.isNotEmpty()) {
                        onSaveHistory(hasil)
                        Toast.makeText(context, "Hasil disalin & disimpan", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Salin & Simpan Riwayat", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text("Kembali", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatScreen(navController: NavController, riwayatList: List<String>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 24.dp, vertical = 8.dp)) {
            if (riwayatList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada riwayat.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                    items(riwayatList) { riwayat ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                text = riwayat,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DaftarBilanganPreview() {
    BilanganKuTheme {
        val navController = rememberNavController()
        AppNavigation(navController)
    }
}