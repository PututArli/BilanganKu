package com.example.bilanganku.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Penerjemah : Screen("penerjemah")
    object Konversi : Screen("konversi")
    object Riwayat : Screen("riwayat")
    object Detail : Screen("detail/{nama}") {
        fun createRoute(nama: String) = "detail/$nama"
    }
}
