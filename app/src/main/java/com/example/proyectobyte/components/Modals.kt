package com.example.proyectobyte.components

import android.widget.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button

@Composable
fun LoginModal(
    show: Boolean,
    onDismiss: () -> Unit
){
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Aviso") },
            text = {Text("No puedes añadir productos al carrito sin haber iniciado sesion.")},
            confirmButton = {
                Button(onClick = onDismiss){
                    Text("Aceptar")
                }
            }
        )
    }
}