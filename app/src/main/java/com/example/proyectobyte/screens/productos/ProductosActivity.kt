package com.example.proyectobyte.screens.productos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.proyectobyte.FilaProductoItem
import com.example.proyectobyte.components.LoginModal
import com.example.proyectobyte.ui.theme.AppTheme
import com.example.proyectobyte.ui.theme.ProyectoByteTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Producto(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val descuento: String = "",
    val marca: String = "",
    val imagen: String = "",
    val categoria: String = ""
)

data class ApiResponse(
    val status: String? = null,
    val data: List<Producto>
)

interface ApiService {
    @GET( "productos.php")
    suspend fun getProductos() : ApiResponse

}

object RetrofitClient {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://adrbo.alwaysdata.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)

    }
}

class ProductosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoByteTheme() {
                ProductosScreen(
                    onVolverClick = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    onVolverClick: () -> Unit = {}
) {
    var listaProductos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    var showLoginDialog by remember { mutableStateOf(false) }

    // Consumo de datos al abrir la pantalla exclusiva
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.api.getProductos()
            listaProductos = response.data
        } catch (e: Exception) {
            mensajeError = "Error al conectar: ${e.localizedMessage}"
        } finally {
            cargando = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo Completo") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppTheme.spacing.medium)
        ) {
            when {
                cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                mensajeError != null -> {
                    Text(
                        text = mensajeError!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)
                    ) {
                        items(listaProductos) { producto ->
                            FilaProductoItem(producto = producto,
                                onAddClick = {
                                    showLoginDialog = true
                                })
                            HorizontalDivider(
                                modifier = Modifier.padding(top = AppTheme.spacing.small),
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            )
                        }
                    }
                    LoginModal(
                        show = showLoginDialog,
                        onDismiss = {showLoginDialog = false}
                    )
                }
            }
        }
    }
}