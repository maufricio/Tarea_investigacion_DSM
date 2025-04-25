import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.net.Uri
import android.widget.Toast
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.dsm.lugaresudb.datos.Departamento
import com.dsm.lugaresudb.datos.RepositorioDepartamentos
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


@Composable
fun AgregarDepartamentoScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var servicios by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var mensaje by remember { mutableStateOf("") }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagenUri = uri
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Agregar Departamento", style = MaterialTheme.typography.bodySmall)

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = servicios,
            onValueChange = { servicios = it },
            label = { Text("Servicios que ofrece") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleccionar imagen")
        }

        imagenUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Button(
            onClick = {
                if (imagenUri == null) {
                    Toast.makeText(context, "Selecciona una imagen", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val storageRef = FirebaseStorage.getInstance().reference
                val nombreImagen = UUID.randomUUID().toString()
                val imagenRef = storageRef.child("departamentos/$nombreImagen.jpg")

                imagenRef.putFile(imagenUri!!)
                    .addOnSuccessListener {
                        imagenRef.downloadUrl.addOnSuccessListener { uri ->
                            val nuevoDepartamento = Departamento(
                                nombre = nombre,
                                descripcion = descripcion,
                                servicios = servicios,
                                imagenUrl = uri.toString()
                            )

                            RepositorioDepartamentos.guardarDepartamento(
                                nuevoDepartamento,
                                onSuccess = {
                                    mensaje = "Departamento guardado con éxito"
                                },
                                onError = {
                                    mensaje = "Error al guardar: ${it.message}"
                                }
                            )
                        }
                    }
                    .addOnFailureListener {
                        mensaje = "Error al subir imagen: ${it.message}"
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        Button(onClick = {
            navController.navigate("main") {
                popUpTo("agregar") { inclusive = true }
            }
        }) {
            Text("Volver al inicio")
        }

        if (mensaje.isNotEmpty()) {
            Text(text = mensaje, color = MaterialTheme.colorScheme.primary)
        }
    }
}
