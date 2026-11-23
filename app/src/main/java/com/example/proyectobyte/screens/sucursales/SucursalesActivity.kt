package com.example.proyectobyte.screens.sucursales

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectobyte.models.Sucursal
import com.example.proyectobyte.ui.theme.AppTheme
import com.example.proyectobyte.ui.theme.ProyectoByteTheme

class SucursalesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[SucursalesViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            ProyectoByteTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                SucursalesScreen(
                    uiState = uiState,
                    onBackClick = { finish() },
                    onReintentar = { viewModel.fetchSucursales() },
                    onVerMapaClick = {
                        startActivity(
                            Intent(this@SucursalesActivity, MapaSucursalesActivity::class.java)
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SucursalesScreen(
    uiState: SucursalesUIState,
    onBackClick: () -> Unit,
    onReintentar: () -> Unit,
    onVerMapaClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Nuestras tiendas",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            "Recoge tu pedido en Lima",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState is SucursalesUIState.Success) {
                ExtendedFloatingActionButton(
                    onClick = onVerMapaClick,
                    icon = { Icon(Icons.Outlined.Map, contentDescription = null) },
                    text = { Text("Ver mapa completo") }
                )
            }
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
                        "No se pudieron cargar las tiendas",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(AppTheme.spacing.medium))
                    FilledTonalButton(onClick = onReintentar) { Text("Reintentar") }
                }

                is SucursalesUIState.Success -> ListaSucursales(uiState.sucursales)
            }
        }
    }
}

@Composable
private fun ListaSucursales(sucursales: List<Sucursal>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = AppTheme.spacing.regular,
            end = AppTheme.spacing.regular,
            top = AppTheme.spacing.small,
            bottom = 96.dp
        ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
    ) {
        items(sucursales, key = { it.id }) { sucursal ->
            SucursalCard(sucursal)
        }
    }
}
