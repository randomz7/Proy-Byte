package com.example.proyectobyte.screens.productos

import com.example.proyectobyte.models.Producto

sealed interface ProductosUIState {
    data object Loading : ProductosUIState
    data class Success(val productos: List<Producto>) : ProductosUIState
    data class Error(val mensaje: String) : ProductosUIState
}
