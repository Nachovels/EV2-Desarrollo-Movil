package com.example.tcgstore.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcgstore.data.ItemCarrito
import com.example.tcgstore.data.Producto
import com.example.tcgstore.data.UserStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CarritoViewModel(private val userStorage: UserStorage) : ViewModel() {

    private val _itemsCarrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val itemsCarrito: StateFlow<List<ItemCarrito>> = _itemsCarrito.asStateFlow()

    init {
        viewModelScope.launch {
            userStorage.carritoFlow.collect { carrito ->
                _itemsCarrito.value = carrito
            }
        }
    }

    fun agregarAlCarrito(producto: Producto) {
        viewModelScope.launch {
            val carritoActual = userStorage.carritoFlow.first().toMutableList()
            val itemExistente = carritoActual.find { it.producto.nombre == producto.nombre }

            if (itemExistente != null) {
                val itemActualizado = itemExistente.copy(cantidad = itemExistente.cantidad + 1)
                val itemIndex = carritoActual.indexOf(itemExistente)
                carritoActual[itemIndex] = itemActualizado
            } else {
                carritoActual.add(ItemCarrito(producto = producto, cantidad = 1))
            }
            userStorage.guardarCarrito(carritoActual)
        }
    }

    fun borrarDelCarrito(itemCarrito: ItemCarrito) {
        viewModelScope.launch {
            val carritoActual = userStorage.carritoFlow.first().toMutableList()
            carritoActual.remove(itemCarrito)
            userStorage.guardarCarrito(carritoActual)
        }
    }

    fun aumentarCantidad(itemCarrito: ItemCarrito) {
        viewModelScope.launch {
            val carritoActual = userStorage.carritoFlow.first().toMutableList()
            val itemExistente = carritoActual.find { it.producto.nombre == itemCarrito.producto.nombre }

            if (itemExistente != null) {
                val itemActualizado = itemExistente.copy(cantidad = itemExistente.cantidad + 1)
                val itemIndex = carritoActual.indexOf(itemExistente)
                carritoActual[itemIndex] = itemActualizado
                userStorage.guardarCarrito(carritoActual)
            }
        }
    }

    fun disminuirCantidad(itemCarrito: ItemCarrito) {
        viewModelScope.launch {
            val carritoActual = userStorage.carritoFlow.first().toMutableList()
            val itemExistente = carritoActual.find { it.producto.nombre == itemCarrito.producto.nombre }

            if (itemExistente != null) {
                if (itemExistente.cantidad > 1) {
                    val itemActualizado = itemExistente.copy(cantidad = itemExistente.cantidad - 1)
                    val itemIndex = carritoActual.indexOf(itemExistente)
                    carritoActual[itemIndex] = itemActualizado
                } else {
                    carritoActual.remove(itemExistente)
                }
                userStorage.guardarCarrito(carritoActual)
            }
        }
    }

    fun borrarCarrito() {
        viewModelScope.launch {
            userStorage.vaciarCarrito()
        }
    }
}
