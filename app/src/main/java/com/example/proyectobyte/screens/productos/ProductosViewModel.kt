package com.example.proyectobyte.screens.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobyte.data.repository.ProductosRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductosViewModel(
    private val repository: ProductosRepository = ProductosRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProductosUIState>(ProductosUIState.Loading)
    val uiState: StateFlow<ProductosUIState> = _uiState.asStateFlow()

    fun fetchProductos() {
        viewModelScope.launch {
            _uiState.value = ProductosUIState.Loading
            _uiState.value = try {
                ProductosUIState.Success(repository.listProductos())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                ProductosUIState.Error(
                    "No se pudieron cargar los datos. Revise su conexión o intente de nuevo: "
                            + e.message
                )
            }
        }
    }

    init {
        fetchProductos()
    }
}
