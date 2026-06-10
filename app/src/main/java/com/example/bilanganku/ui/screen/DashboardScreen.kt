package com.example.bilanganku.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bilanganku.ui.component.MenuCard
import com.example.bilanganku.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BilanganKu", fontWeight = FontWeight.ExtraBold, fontSize = 28.sp) },
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    text = "Pilih Menu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            item {
                MenuCard(
                    title = "Konversi Basis Bilangan",
                    subtitle = "Desimal, Biner, Oktal, Hexadesimal",
                    icon = Icons.Default.Calculate,
                    color = Color(0xFF4F46E5),
                    onClick = { navController.navigate(Screen.Konversi.route) }
                )
            }
            item {
                MenuCard(
                    title = "Sandi Teks",
                    subtitle = "Terjemahkan teks ke ASCII & Base64",
                    icon = Icons.Default.Translate,
                    color = Color(0xFF10B981),
                    onClick = { navController.navigate(Screen.Penerjemah.route) }
                )
            }
            item {
                MenuCard(
                    title = "Riwayat Aktivitas",
                    subtitle = "Lihat hasil konversi yang disimpan",
                    icon = Icons.Default.History,
                    color = Color(0xFFF59E0B),
                    onClick = { navController.navigate(Screen.Riwayat.route) }
                )
            }
        }
    }
}
