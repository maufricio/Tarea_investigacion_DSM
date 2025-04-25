package com.dsm.lugaresudb

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dsm.lugaresudb.datos.DepartamentoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDepartamentosScreen(navController: NavController, viewModel: DepartamentoViewModel = viewModel()) {
    val departamentos by viewModel.departamentos.collectAsState()

    // Cargar datos al iniciar
    LaunchedEffect(Unit) {
        viewModel.cargarDepartamentos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Departamentos") },
                navigationIcon = { // <--- AQUI el icono de regreso
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            items(departamentos) { departamento ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Nombre: ${departamento.nombre}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Descripción: ${departamento.descripcion}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }

}