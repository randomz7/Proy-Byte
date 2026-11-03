package com.example.proyectobyte

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.proyectobyte.screens.productos.Producto
import com.example.proyectobyte.screens.productos.ProductosActivity
import com.example.proyectobyte.screens.productos.RetrofitClient
import com.example.proyectobyte.ui.theme.AppTheme
import com.example.proyectobyte.ui.theme.ProyectoByteTheme
import com.example.proyectobyte.components.LoginModal
import com.example.proyectobyte.screens.ofertas.OfertasActivity

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoByteTheme {
                MenuScreen(
                    onLoginClick = {
                        // Preparado para conectar navegación a Login
                    },
                    onCarritoClick = {
                        // Preparado para conectar navegación a Carrito
                    },
                    onVerTodosClick = {
                        startActivity(Intent(this@MenuActivity, ProductosActivity::class.java))
                    },
                    onOfertasClick = {
                        startActivity(Intent(this@MenuActivity, OfertasActivity::class.java))
                    }
                )
            }
        }
    }
}

data class OfertaDummy(
    val id: Int,
    val titulo: String,
    val descuento: String,
    val imagenRes: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onLoginClick: () -> Unit = {},
    onCarritoClick: () -> Unit = {},
    onVerTodosClick: () -> Unit = {},
    onOfertasClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    // 1. Guardamos el texto en una variable primero
    val textoCategoriaTodos = stringResource(R.string.cat_todos)

    var listaProductos by remember { mutableStateOf<List<Producto>>(emptyList()) }

    var showLoginDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.api.getProductos()
            listaProductos = response.data.take(5)
        } catch (e: Exception) {
        }
    }
    // 2. Se lo pasamos a remember
    var categoriaSeleccionada by remember { mutableStateOf(textoCategoriaTodos) }
    val categorias = listOf(
        stringResource(R.string.cat_todos),
        stringResource(R.string.cat_consolas),
        stringResource(R.string.cat_videojuegos),
        stringResource(R.string.cat_mandos),
        stringResource(R.string.cat_sillas),
        stringResource(R.string.cat_accesorios)
    )

    val ofertasEjemplo = listOf(
        OfertaDummy(1, "Combo PS5 Slim", "Ahorra $100", imagenRes = R.drawable.ps5_slim),
        OfertaDummy(2, "Mandos Xbox Wireless", "30% OFF", imagenRes = R.drawable.xbox_control),
        OfertaDummy(3, "Preventa GTA VI", "Reserva ya", imagenRes = R.drawable.gtavi)
    )

    val productosLimitados = listaProductos.take(5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onLoginClick) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = stringResource(R.string.iniciar_sesion)
                        )
                    }
                    IconButton(onClick = onCarritoClick) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = stringResource(R.string.carrito_compras)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
        ) {
            // 1. Buscador
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.buscar_placeholder)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(AppTheme.sizes.heroCornerRadius)
                )
            }

            // 2. Banner de Ofertas Destacadas
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                Text(
                    text = stringResource(R.string.ofertas_destacadas),
                    style = MaterialTheme.typography.titleLarge
                )
                TextButton(onClick = onOfertasClick) {
                    Text(
                        text = "Ver más",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                }
                Spacer(modifier = Modifier.height(AppTheme.spacing.small))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
                ) {
                    items(ofertasEjemplo) { oferta ->
                        BannerOfertaItem(oferta = oferta)
                    }
                }
            }

            // 3. Filtro por Categorías
            item {
                Text(
                    text = stringResource(R.string.categorias),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(AppTheme.spacing.micro))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)
                ) {
                    items(categorias) { cat ->
                        FilterChip(
                            selected = categoriaSeleccionada == cat,
                            onClick = { categoriaSeleccionada = cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }

            // 4. Encabezado de Productos con botón "Ver todos"
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.productos_populares),
                        style = MaterialTheme.typography.titleLarge
                    )
                    TextButton(onClick = onVerTodosClick) {
                        Text(
                            text = stringResource(R.string.ver_todos),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            // 5. Lista de Productos en formato plano/tabla (Máximo 5)
            items(productosLimitados) { producto ->
                FilaProductoItem(producto = producto,
                    onAddClick = {
                        showLoginDialog = true
                    })
                HorizontalDivider(
                    modifier = Modifier.padding(top = AppTheme.spacing.small),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            }

            item {
                Spacer(modifier = Modifier.height(AppTheme.spacing.large))
            }
        }
    }
    LoginModal(
        show = showLoginDialog,
        onDismiss = {showLoginDialog = false}
    )
}

@Composable
fun BannerOfertaItem(
    oferta: OfertaDummy
) {
    Box(
        modifier = Modifier
            .width(260.dp)
            .height(AppTheme.sizes.dashboardCardHeight)
            .clip(RoundedCornerShape(AppTheme.sizes.heroCornerRadius))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        if (oferta.imagenRes != null) {
            Image(
                painter = painterResource(id = oferta.imagenRes),
                contentDescription = oferta.titulo,
                modifier = Modifier.fillMaxSize().padding(AppTheme.spacing.medium),
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppTheme.spacing.medium),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = oferta.titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = oferta.descuento,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun FilaProductoItem(
    producto: Producto,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Contenedor de la imagen
        Box(
            modifier = Modifier
                .size(AppTheme.sizes.iconContainerLarge)
                .clip(RoundedCornerShape(AppTheme.sizes.heroCornerRadius))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (producto.imagen.isNotEmpty()) {
                // Si usas Coil en tu proyecto:
                AsyncImage(
                    model = producto.imagen,
                    contentDescription = producto.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(AppTheme.sizes.iconMedium)
                )
            }
        }

        Spacer(modifier = Modifier.width(AppTheme.spacing.medium))

        // Detalles del producto de la API
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            if (producto.categoria.isNotEmpty()) {
                Text(
                    text = producto.categoria,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Text(
                text = "S/ ${producto.precio}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Acciones
        IconButton(onClick = onAddClick) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.agregar_al_carrito),
                modifier = Modifier.size(AppTheme.sizes.iconMedium)
            )
        }
    }
}