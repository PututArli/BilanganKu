package com.example.bilanganku.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bilanganku.ui.screen.*
import com.example.bilanganku.ui.viewmodel.MainViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val viewModel: MainViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }
        composable(Screen.Penerjemah.route) {
            PenerjemahTeksScreen(navController)
        }
        composable(Screen.Konversi.route) {
            DaftarBilanganScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Screen.Detail.route) { backStackEntry ->
            val nama = backStackEntry.arguments?.getString("nama")
            val sistem = viewModel.dataList.find { it.nama == nama }
            if (sistem != null) {
                DetailBilanganScreen(
                    sistem = sistem,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
        composable(Screen.Riwayat.route) {
            RiwayatScreen(navController, viewModel.riwayatList)
        }
    }
}
