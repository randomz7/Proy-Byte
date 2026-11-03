package com.example.proyectobyte.screens.ofertas

import android.graphics.pdf.models.ListItem
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyectobyte.OfertaDummy
import com.example.proyectobyte.R
import com.example.proyectobyte.components.LoginModal
import com.example.proyectobyte.screens.ofertas.ui.theme.ProyectoByteTheme
import com.example.proyectobyte.ui.theme.AppTheme

class OfertasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoByteTheme {
                OfertasScreen (
                    onVolverClick = {finish()}
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfertasScreen(
    onVolverClick: () -> Unit = {}
){
    val ofertasEjemplo = listOf(
        OfertaDummy(1, "Combo PS5 Slim", "Ahorra $100", imagenRes = R.drawable.ps5_slim),
        OfertaDummy(2, "Mandos Xbox Wireless", "30% OFF", imagenRes = R.drawable.xbox_control),
        OfertaDummy(3, "Preventa GTA VI", "Reserva ya", imagenRes = R.drawable.gtavi)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ofertas Destacadas") },
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
    ){
        innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
        ) {
            items(ofertasEjemplo){ oferta ->
                CardOferta(oferta = oferta)
            }
        }
    }
}


@Composable
fun CardOferta(oferta: OfertaDummy){

    var showLoginDialog by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = AppTheme.spacing.medium)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            Text(
                text = oferta.titulo,
                style = MaterialTheme.typography.titleLarge
            )

                IconButton(onClick = {showLoginDialog = true}) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.agregar_al_carrito),
                        modifier = Modifier.size(AppTheme.sizes.iconMedium)
                    )
                }
            }
            Box(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    .padding(AppTheme.spacing.small)
            ){
            Text(
                text = oferta.descuento,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = AppTheme.spacing.small),
                color = MaterialTheme.colorScheme.tertiary
            )
            }

            if (oferta.imagenRes != null) {
                Image(
                    painter = painterResource(id = oferta.imagenRes),
                    contentDescription = oferta.titulo,
                    modifier = Modifier.fillMaxSize().padding(AppTheme.spacing.medium),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
    LoginModal(
        show = showLoginDialog,
        onDismiss = {showLoginDialog = false}
    )
}