package com.example.proyectobyte.screens.productos

import com.example.proyectobyte.models.Producto

sealed interface DetalleProductoUIState {
    data object Loading : DetalleProductoUIState
    data class Success(val producto: Producto) : DetalleProductoUIState
    data class Error(val mensaje: String) : DetalleProductoUIState
}
