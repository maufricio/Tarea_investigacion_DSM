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
}
