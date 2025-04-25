package com.dsm.lugaresudb

import AgregarDepartamentoScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dsm.lugaresudb.ui.theme.LugaresUdbEjemplo4Theme
import com.google.firebase.FirebaseApp
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            LugaresUdbEjemplo4Theme {
                AppNavigation()
            }
        }
    }
}
@Composable
fun MainScreen(navController: NavController) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            androidx.compose.material3.Button(onClick = {
                navController.navigate("agregar")
            }) {
                androidx.compose.material3.Text("Ir a Agregar Departamento")
            }

            androidx.compose.material3.Button(onClick = {
                navController.navigate("listar")
            }) {
                androidx.compose.material3.Text("Ver Lista de Departamentos")
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("agregar") { AgregarDepartamentoScreen(navController) }
        composable("listar") { ListaDepartamentosScreen(navController) }
    }
}
