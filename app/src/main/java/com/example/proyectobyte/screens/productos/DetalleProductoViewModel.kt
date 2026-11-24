package com.example.proyectobyte.screens.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobyte.data.repository.ProductosRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleProductoViewModel(
    private val repository: ProductosRepository = ProductosRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetalleProductoUIState>(DetalleProductoUIState.Loading)
    val uiState: StateFlow<DetalleProductoUIState> = _uiState.asStateFlow()

    fun fetchProducto(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetalleProductoUIState.Loading
            _uiState.value = try {
                DetalleProductoUIState.Success(repository.getProducto(id))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                DetalleProductoUIState.Error(
                    "No se pudieron cargar los datos. Revise su conexión o intente de nuevo: "
                            + e.message
                )
            }
        }
    }
}
