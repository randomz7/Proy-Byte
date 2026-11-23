package com.example.proyectobyte.screens.sucursales

import com.example.proyectobyte.models.Sucursal

sealed interface SucursalesUIState {
    data object Loading : SucursalesUIState
    data class Success(val sucursales: List<Sucursal>) : SucursalesUIState
    data class Error(val mensaje: String) : SucursalesUIState
}
