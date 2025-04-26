package com.dsm.lugaresudb.datos

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


object RepositorioDepartamentos {

    fun guardarDepartamento(departamento: Departamento, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("departamentos")
            .add(departamento)
            .addOnSuccessListener {
                Log.d("Firebase", "Departamento guardado con ID: ${it.id}")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error al guardar", e)
                onError(e)
            }
    }


    fun obtenerDepartamentos(
        onSuccess: (List<Departamento>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        db.collection("departamentos")
            .get()
            .addOnSuccessListener { result ->
                val departamentos = result.mapNotNull { doc ->
                    val departamento = doc.toObject(Departamento::class.java)
                    departamento.id = doc.id
                    departamento
                }
                onSuccess(departamentos)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }


    fun eliminarDepartamento(
        id: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        db.collection("departamentos")
            .document(id)
            .delete()
            .addOnSuccessListener {
                Log.d("Firebase", "Departamento eliminado con ID: $id")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error al eliminar", e)
                onError(e)
            }
    }

    fun actualizarDepartamento(
        departamento: Departamento,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        db.collection("departamentos")
            .document(departamento.id)
            .set(departamento)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }

}
