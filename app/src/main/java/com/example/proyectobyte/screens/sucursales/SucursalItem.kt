package com.example.proyectobyte.screens.sucursales

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.proyectobyte.models.Sucursal
import com.example.proyectobyte.ui.theme.AppTheme

@Composable
fun SucursalCard(sucursal: Sucursal) {
    val uriHandler = LocalUriHandler.current
    val telefono = sucursal.telefono.filter { it.isDigit() || it == '+' }
    val mapa = "https://www.google.com/maps/search/?api=1&query=${sucursal.latitud},${sucursal.longitud}"

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cardCornerRadius),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(AppTheme.sizes.cardElevation)
    ) {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.regular)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(AppTheme.sizes.iconContainerMedium)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Outlined.Storefront,
                        null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column(Modifier.padding(start = AppTheme.spacing.medium).weight(1f)) {
                    Text(
                        sucursal.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        "${sucursal.distrito} · Lima",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(.12f)
            )
            Spacer(Modifier.height(AppTheme.spacing.medium))

            Text(
                sucursal.direccion,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(AppTheme.spacing.small))

            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)) {
                OutlinedButton(
                    onClick = { runCatching { uriHandler.openUri("tel:$telefono") } },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Outlined.Phone, null, Modifier.size(AppTheme.sizes.iconSmall))
                    Text("Llamar", Modifier.padding(start = AppTheme.spacing.small))
                }
                FilledTonalButton(
                    onClick = { runCatching { uriHandler.openUri(mapa) } },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Outlined.Map, null, Modifier.size(AppTheme.sizes.iconSmall))
                    Text("Ver mapa", Modifier.padding(start = AppTheme.spacing.small))
                }
            }
        }
    }
}
