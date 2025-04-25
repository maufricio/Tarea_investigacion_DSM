package com.dsm.lugaresudb.datos

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DepartamentoViewModel : ViewModel() {
    private val _departamentos = MutableStateFlow<List<Departamento>>(emptyList())
    val departamentos: StateFlow<List<Departamento>> = _departamentos

    fun cargarDepartamentos() {
        RepositorioDepartamentos.obtenerDepartamentos(
            onSuccess = { lista ->
                _departamentos.value = lista
            },
            onError = { exception ->
                // Podrías manejar errores aquí
                exception.printStackTrace()
            }
        )
    }
}