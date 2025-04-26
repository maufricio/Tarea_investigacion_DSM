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
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.dsm.lugaresudb.datos.Departamento
import com.dsm.lugaresudb.datos.RepositorioDepartamentos
import com.dsm.lugaresudb.datos.RetrofitClient
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Completa los datos del departamento",
                style = MaterialTheme.typography.headlineSmall
            )

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

            Button(
                onClick = { launcher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
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

                    val contentResolver = context.contentResolver
                    val stream = contentResolver.openInputStream(imagenUri!!)
                    val requestFile = stream!!.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
                    val body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile)
                    val preset = "departamentos".toRequestBody("text/plain".toMediaTypeOrNull())

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = RetrofitClient.cloudinaryApi.uploadImage(body, preset)
                            if (response.isSuccessful) {
                                val imageUrl = response.body()?.secure_url ?: ""
                                Log.d("Cloudinary", "Imagen subida: $imageUrl")

                                val nuevoDepartamento = Departamento(
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    servicios = servicios,
                                    imagenUrl = imageUrl
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
                            } else {
                                Log.e("Cloudinary", "Error: ${response.errorBody()?.string()}")
                            }
                        } catch (e: Exception) {
                            Log.e("Cloudinary", "Excepción al subir: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }

            Button(
                onClick = {
                    navController.navigate("main") {
                        popUpTo("agregar") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al inicio")
            }

            if (mensaje.isNotEmpty()) {
                Text(text = mensaje, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
