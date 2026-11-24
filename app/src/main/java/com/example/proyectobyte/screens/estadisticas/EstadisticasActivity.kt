package com.example.proyectobyte.screens.estadisticas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import com.example.proyectobyte.models.Dato
import com.example.proyectobyte.ui.theme.AppTheme
import com.example.proyectobyte.ui.theme.ProyectoByteTheme

class EstadisticasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[EstadisticasViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            ProyectoByteTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                EstadisticasScreen(
                    uiState = uiState,
                    onBackClick = { finish() },
                    onReintentar = { viewModel.fetchEstadisticas() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EstadisticasScreen(
    uiState: EstadisticasUIState,
    onBackClick: () -> Unit,
    onReintentar: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Estadísticas",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            "Inventario de la tienda",
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
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                EstadisticasUIState.Loading -> CircularProgressIndicator(
                    Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.tertiary
                )

                is EstadisticasUIState.Error -> Column(
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
                        "No se pudieron cargar las estadísticas",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(AppTheme.spacing.medium))
                    FilledTonalButton(onClick = onReintentar) { Text("Reintentar") }
                }

                is EstadisticasUIState.Success -> PanelGrafico(uiState.datos)
            }
        }
    }
}

@Composable
private fun PanelGrafico(datos: List<Dato>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppTheme.spacing.medium)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppTheme.sizes.cardCornerRadius),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(AppTheme.sizes.cardElevation)
        ) {
            Column(modifier = Modifier.padding(AppTheme.spacing.regular)) {
                Text(
                    "Stock disponible por marca",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Las ${datos.size} marcas con más unidades en inventario",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.height(AppTheme.spacing.regular))
                if (datos.isNotEmpty()) {
                    GraficoBarrasHorizontal(datos = datos)
                }
            }
        }
    }
}
