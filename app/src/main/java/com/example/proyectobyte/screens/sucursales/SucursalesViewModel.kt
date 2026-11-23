package com.example.proyectobyte.screens.sucursales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobyte.data.repository.SucursalesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SucursalesViewModel(
    private val repository: SucursalesRepository = SucursalesRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<SucursalesUIState>(SucursalesUIState.Loading)
    val uiState: StateFlow<SucursalesUIState> = _uiState.asStateFlow()

    fun fetchSucursales() {
        viewModelScope.launch {
            _uiState.value = SucursalesUIState.Loading
            _uiState.value = try {
                SucursalesUIState.Success(repository.listSucursales())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                SucursalesUIState.Error(
                    "No se pudieron cargar los datos. Revise su conexión o intente de nuevo: "
                            + e.message
                )
            }
        }
    }

    init {
        fetchSucursales()
    }
}
