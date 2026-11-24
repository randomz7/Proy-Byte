package com.example.proyectobyte.screens.estadisticas

import com.example.proyectobyte.models.Dato

sealed interface EstadisticasUIState {
    data object Loading : EstadisticasUIState
    data class Success(val datos: List<Dato>) : EstadisticasUIState
    data class Error(val mensaje: String) : EstadisticasUIState
}
