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
import com.dsm.lugaresudb.datos.Departamento


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDepartamentosScreen(navController: NavController, viewModel: DepartamentoViewModel = viewModel()) {
    val departamentos by viewModel.departamentos.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var departamentoAEliminar by remember { mutableStateOf<Departamento?>(null) }


    // Cargar datos al iniciar
    LaunchedEffect(Unit) {
        viewModel.cargarDepartamentos()
    }
    //Modal para  eliminar y confirmacion
    if (showDialog && departamentoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmacion") },
            text = { Text("¿Esta seguro que desea eliminar este departamento?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarDepartamento(departamentoAEliminar!!.id)
                        showDialog = false
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("No")
                }
            }
        )
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

                        //  Mostrar la imagen desde URL con la extensión de Picasso
                        if (departamento.imagenUrl.isNotEmpty()) {
                            PicassoImage(
                                url = departamento.imagenUrl,
                                contentDescription = "Imagen del departamento",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Text(text = "Nombre: ${departamento.nombre}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Descripción: ${departamento.descripcion}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Servicios: ${departamento.servicios}", style = MaterialTheme.typography.bodyMedium)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Botón Eliminar
                            Button(
                                onClick = { departamentoAEliminar = departamento
                                    showDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Eliminar")
                            }

                            // Botón Editar (amarillo)
                            Button(
                                onClick = { /* TODO: lógica para editar */ },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                            ) {
                                Text("Editar")
                            }
                        }
                    }
                }
            }
        }
    }

}