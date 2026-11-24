package com.example.proyectobyte.screens.productos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.proyectobyte.R
import com.example.proyectobyte.components.LoginModal
import com.example.proyectobyte.models.Producto
import com.example.proyectobyte.ui.theme.AppTheme
import com.example.proyectobyte.ui.theme.ProyectoByteTheme

class DetalleProductoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idProducto = intent.getIntExtra("idproducto", 0)
        val viewModel = ViewModelProvider(this)[DetalleProductoViewModel::class.java]
        if (savedInstanceState == null) {
            viewModel.fetchProducto(idProducto)
        }
        enableEdgeToEdge()
        setContent {
            ProyectoByteTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                DetalleProductoScreen(
                    uiState = uiState,
                    onBackClick = { finish() },
                    onReintentar = { viewModel.fetchProducto(idProducto) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetalleProductoScreen(
    uiState: DetalleProductoUIState,
    onBackClick: () -> Unit,
    onReintentar: () -> Unit
) {
    var showLoginDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Detalle del producto") },
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
                DetalleProductoUIState.Loading -> CircularProgressIndicator(
                    Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.tertiary
                )

                is DetalleProductoUIState.Error -> Column(
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
                        "No se pudo cargar el producto",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(AppTheme.spacing.medium))
                    FilledTonalButton(onClick = onReintentar) { Text("Reintentar") }
                }

                is DetalleProductoUIState.Success -> DetalleProducto(
                    producto = uiState.producto,
                    onAgregarClick = { showLoginDialog = true }
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
private fun DetalleProducto(
    producto: Producto,
    onAgregarClick: () -> Unit
) {
    val fotos = listOfNotNull(producto.imagen, producto.imagen2, producto.imagen3)
        .filter { it.isNotBlank() }
    val pagerState = rememberPagerState(pageCount = { fotos.size })
    val descuento = producto.descuento.toDoubleOrNull() ?: 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
    ) {
        if (fotos.isNotEmpty()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(AppTheme.sizes.heroCornerRadius))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) { pagina ->
                val FOTO_URL = "https://adrbo.alwaysdata.net/imagenes/" + fotos[pagina]
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(FOTO_URL)
                        .crossfade(true)
                        .build(),
                    contentDescription = producto.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(fotos.size) { indice ->
                    val color = if (pagerState.currentPage == indice) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    }
                    Box(
                        Modifier
                            .padding(AppTheme.spacing.extraSmall)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.micro)) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "${producto.categoria} · ${producto.marca}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "S/ ${producto.precio}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            if (descuento > 0) {
                Text(
                    text = "-${producto.descuento}%",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )

        Text(
            text = producto.descripcion,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Stock disponible: ${producto.stock_actual} unidades",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        Button(
            onClick = onAgregarClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppTheme.sizes.heroCornerRadius)
        ) {
            Text(stringResource(R.string.agregar_al_carrito))
        }
    }
}
