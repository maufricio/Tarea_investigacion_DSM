package com.dsm.lugaresudb.datos

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DepartamentoViewModel : ViewModel() {
    private val _departamentos = MutableStateFlow<List<Departamento>>(emptyList())
    val departamentos: StateFlow<List<Departamento>> = _departamentos


    fun cargarDepartamentos() {
        RepositorioDepartamentos.obtenerDepartamentos(
            onSuccess = { lista ->
                _departamentos.value = lista
            },
            onError = { exception ->
                exception.printStackTrace()
            }
        )
    }
    fun eliminarDepartamento(id: String) {
        viewModelScope.launch {
            RepositorioDepartamentos.eliminarDepartamento(
                id,
                onSuccess = {
                    cargarDepartamentos() // carga la lista actualizada
                },
                onError = { exception ->
                    exception.printStackTrace()
                }
            )
        }
    }

    fun actualizarDepartamento(departamentoActualizado: Departamento) {
        viewModelScope.launch {
            RepositorioDepartamentos.actualizarDepartamento(
                departamentoActualizado,
                onSuccess = {
                    // Actualiza la lista local después de que se actualice en Firebase
                    _departamentos.value = _departamentos.value.map { departamento ->
                        if (departamento.id == departamentoActualizado.id) {
                            departamentoActualizado
                        } else {
                            departamento
                        }
                    }
                },
                onError = { exception ->
                    exception.printStackTrace()
                }
            )
        }
    }
}