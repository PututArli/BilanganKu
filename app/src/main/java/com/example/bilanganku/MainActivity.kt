package com.example.bilanganku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.bilanganku.data.model.SistemBilangan
import com.example.bilanganku.data.repository.BilanganRepository
import com.example.bilanganku.ui.theme.BilanganKuTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BilanganKuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF8FAFC)
                ) {
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
    var dataList by remember { mutableStateOf<List<SistemBilangan>>(emptyList()) }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            DaftarBilanganScreen(
                navController = navController,
                inputValue = inputValue,
                onInputValueChange = { inputValue = it },
                inputBase = inputBase,
                onInputBaseChange = { inputBase = it },
                dataList = dataList,
                onDataLoaded = { dataList = it }
            )
        }
        composable("detail/{nama}") { backStackEntry ->
            val nama = backStackEntry.arguments?.getString("nama")
            val sistem = dataList.find { it.nama == nama }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarBilanganScreen(
    navController: NavController,
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    inputBase: Int,
    onInputBaseChange: (Int) -> Unit,
    dataList: List<SistemBilangan>,
    onDataLoaded: (List<SistemBilangan>) -> Unit
) {
    val repository = remember { BilanganRepository() }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (dataList.isEmpty()) {
            val result = repository.getSistemBilangan()
            if (result.isNotEmpty()) {
                onDataLoaded(result)
                isLoading = false
                isError = false
            } else {
                isLoading = false
                isError = true
            }
        } else {
            isLoading = false
        }
    }

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
                title = { Text("BilanganKu", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp) },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("riwayat") },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF8FAFC),
                    titleContentColor = Color(0xFF0F172A)
                )
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
                            text = "Basis Input",
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
                                        onInputBaseChange(sistem.basis)
                                        onInputValueChange("")
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
                                    onInputValueChange(newValue.uppercase())
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
                                    IconButton(onClick = { onInputValueChange("") }) {
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
                        BilanganListItem(data, inputValue, inputBase, navController)
                    }
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
    val tintColor = remember(sistem.warnaHex) { Color(android.graphics.Color.parseColor(sistem.warnaHex)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable { navController.navigate("detail/${sistem.nama}") },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(tintColor.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = sistem.imageUrl,
                    contentDescription = sistem.nama,
                    modifier = Modifier.size(36.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = sistem.nama, fontSize = 14.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = hasil, fontSize = 22.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.ExtraBold)
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
    val hasil = remember(input, currentBase) { convertUniversal(input, currentBase, sistem.basis) }
    val cardColor = remember(sistem.warnaHex) { Color(android.graphics.Color.parseColor(sistem.warnaHex)) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(sistem.nama, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF8FAFC),
                    titleContentColor = Color(0xFF0F172A)
                )
            )
        },
        containerColor = Color(0xFFF8FAFC),
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
                            isLoading = true
                            delay(1500)
                            onSaveHistory(hasil)
                            isLoading = false
                            snackbarHostState.showSnackbar("Disimpan ke riwayat!")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                enabled = !isLoading
            ) {
                if (isLoading) {
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
                    containerColor = Color(0xFFF8FAFC),
                    titleContentColor = Color(0xFF0F172A)
                )
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (riwayatList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada data konversi.", fontSize = 16.sp, color = Color(0xFF94A3B8))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(riwayatList) { riwayat ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = riwayat,
                                modifier = Modifier.padding(24.dp),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF334155)
                            )
                        }
                    }
                }
            }
        }
    }
}