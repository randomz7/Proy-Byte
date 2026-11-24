package com.example.proyectobyte.screens.estadisticas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobyte.data.repository.EstadisticasRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EstadisticasViewModel(
    private val repository: EstadisticasRepository = EstadisticasRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<EstadisticasUIState>(EstadisticasUIState.Loading)
    val uiState: StateFlow<EstadisticasUIState> = _uiState.asStateFlow()

    fun fetchEstadisticas() {
        viewModelScope.launch {
            _uiState.value = EstadisticasUIState.Loading
            _uiState.value = try {
                EstadisticasUIState.Success(repository.listStockPorMarca())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                EstadisticasUIState.Error(
                    "No se pudieron cargar los datos. Revise su conexión o intente de nuevo: "
                            + e.message
                )
            }
        }
    }

    init {
        fetchEstadisticas()
    }
}
