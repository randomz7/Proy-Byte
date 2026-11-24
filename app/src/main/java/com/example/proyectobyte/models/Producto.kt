package com.example.proyectobyte.models

data class Producto(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val descuento: String = "",
    val marca: String = "",
    val imagen: String = "",
    val imagen2: String? = null,
    val imagen3: String? = null,
    val stock_actual: Int = 0,
    val categoria: String = ""
)
