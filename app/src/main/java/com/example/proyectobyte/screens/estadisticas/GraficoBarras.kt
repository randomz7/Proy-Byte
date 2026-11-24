package com.example.proyectobyte.screens.estadisticas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.proyectobyte.models.Dato

@Composable
fun GraficoBarrasHorizontal(
    datos: List<Dato>,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val colorBarra = MaterialTheme.colorScheme.tertiary
    val colorEje = MaterialTheme.colorScheme.outline
    val colorTexto = MaterialTheme.colorScheme.onBackground
    val estiloEtiqueta = MaterialTheme.typography.bodyMedium.copy(color = colorTexto)
    val estiloValor = MaterialTheme.typography.labelLarge.copy(color = colorTexto)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height((datos.size * 52).dp)
    ) {
        val valorMaximo = maxOf(datos.maxOfOrNull { it.valor } ?: 1f, 1f)
        val anchoEtiqueta = size.width * 0.30f
        val anchoValor = size.width * 0.14f
        val inicioBarras = anchoEtiqueta + 8.dp.toPx()
        val largoMaximo = size.width - inicioBarras - anchoValor
        val altoFila = size.height / datos.size
        val altoBarra = altoFila * 0.6f
        val radio = CornerRadius(6.dp.toPx(), 6.dp.toPx())

        drawLine(
            color = colorEje,
            start = Offset(inicioBarras, 0f),
            end = Offset(inicioBarras, size.height),
            strokeWidth = 2f
        )

        datos.forEachIndexed { indice, dato ->
            val posY = altoFila * indice + (altoFila - altoBarra) / 2f
            val largoBarra = dato.valor / valorMaximo * largoMaximo

            drawRoundRect(
                color = colorBarra.copy(alpha = 0.25f),
                topLeft = Offset(inicioBarras, posY),
                size = Size(largoBarra, altoBarra),
                cornerRadius = radio
            )
            drawRoundRect(
                color = colorBarra,
                topLeft = Offset(inicioBarras, posY),
                size = Size(largoBarra, altoBarra),
                cornerRadius = radio,
                style = Stroke(width = 3f)
            )

            val etiqueta = textMeasurer.measure(
                text = dato.etiqueta,
                style = estiloEtiqueta,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                constraints = Constraints(maxWidth = anchoEtiqueta.toInt())
            )
            drawText(
                textLayoutResult = etiqueta,
                topLeft = Offset(0f, posY + (altoBarra - etiqueta.size.height) / 2f)
            )

            val valor = textMeasurer.measure(
                text = dato.valor.toInt().toString(),
                style = estiloValor
            )
            drawText(
                textLayoutResult = valor,
                topLeft = Offset(
                    inicioBarras + largoBarra + 8.dp.toPx(),
                    posY + (altoBarra - valor.size.height) / 2f
                )
            )
        }
    }
}
