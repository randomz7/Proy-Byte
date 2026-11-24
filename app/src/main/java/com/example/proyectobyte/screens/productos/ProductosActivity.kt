package com.example.proyectobyte.screens.productos

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectobyte.components.LoginModal
import com.example.proyectobyte.models.Producto
import com.example.proyectobyte.ui.theme.AppTheme
import com.example.proyectobyte.ui.theme.ProyectoByteTheme

class ProductosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[ProductosViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            ProyectoByteTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                ProductosScreen(
                    uiState = uiState,
                    onBackClick = { finish() },
                    onReintentar = { viewModel.fetchProductos() },
                    onProductoClick = { producto ->
                        startActivity(
                            Intent(this@ProductosActivity, DetalleProductoActivity::class.java)
                                .putExtra("idproducto", producto.id)
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductosScreen(
    uiState: ProductosUIState,
    onBackClick: () -> Unit,
    onReintentar: () -> Unit,
    onProductoClick: (Producto) -> Unit
) {
    var showLoginDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Catálogo Completo") },
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
                ProductosUIState.Loading -> CircularProgressIndicator(
                    Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.tertiary
                )

                is ProductosUIState.Error -> Column(
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
                        "No se pudieron cargar los productos",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(AppTheme.spacing.medium))
                    FilledTonalButton(onClick = onReintentar) { Text("Reintentar") }
                }

                is ProductosUIState.Success -> ListaProductos(
                    productos = uiState.productos,
                    onAddClick = { showLoginDialog = true },
                    onProductoClick = onProductoClick
                )
            }
        }
    }

    LoginModal(
        show = showLoginDialog,
        onDismiss = { showLoginDialog = false }
    )
}

@Composable
private fun ListaProductos(
    productos: List<Producto>,
    onAddClick: () -> Unit,
    onProductoClick: (Producto) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = AppTheme.spacing.medium,
            vertical = AppTheme.spacing.small
        ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)
    ) {
        items(productos, key = { it.id }) { producto ->
            ProductoItem(
                producto = producto,
                onAddClick = onAddClick,
                onClick = { onProductoClick(producto) }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
        }
    }
}
