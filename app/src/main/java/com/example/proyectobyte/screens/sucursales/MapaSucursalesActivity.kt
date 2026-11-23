package com.example.proyectobyte.screens.sucursales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectobyte.models.Sucursal
import com.example.proyectobyte.ui.theme.AppTheme
import com.example.proyectobyte.ui.theme.ProyectoByteTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

class MapaSucursalesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[SucursalesViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            ProyectoByteTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                MapaSucursalesScreen(
                    uiState = uiState,
                    onBackClick = { finish() },
                    onReintentar = { viewModel.fetchSucursales() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapaSucursalesScreen(
    uiState: SucursalesUIState,
    onBackClick: () -> Unit,
    onReintentar: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Mapa de tiendas") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                SucursalesUIState.Loading -> CircularProgressIndicator(
                    Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.tertiary
                )

                is SucursalesUIState.Error -> Column(
                    Modifier
                        .align(Alignment.Center)
                        .padding(AppTheme.spacing.large),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Outlined.CloudOff,
                        contentDescription = null,
                        modifier = Modifier.size(AppTheme.sizes.iconContainerMedium),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(Modifier.height(AppTheme.spacing.large))
                    Text(
                        "No se pudo cargar el mapa",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(AppTheme.spacing.medium))
                    FilledTonalButton(onClick = onReintentar) { Text("Reintentar") }
                }

                is SucursalesUIState.Success -> MapaSucursales(uiState.sucursales)
            }
        }
    }
}

@Composable
private fun MapaSucursales(sucursales: List<Sucursal>) {
    val centroLima = LatLng(-12.06, -77.02)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(centroLima, 11.5f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        sucursales.forEach { sucursal ->
            Marker(
                state = MarkerState(position = LatLng(sucursal.latitud, sucursal.longitud)),
                title = sucursal.nombre,
                snippet = sucursal.direccion
            )
        }
    }
}
