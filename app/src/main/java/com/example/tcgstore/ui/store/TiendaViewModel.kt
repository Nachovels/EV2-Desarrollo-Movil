package com.example.tcgstore.ui.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcgstore.data.network.RetrofitClient
import com.example.tcgstore.data.network.models.ProductResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TiendaViewModel : ViewModel() {

    // Estado que observará la UI (Lista vacía al inicio)
    private val _productos = MutableStateFlow<List<ProductResponse>>(emptyList())
    val productos: StateFlow<List<ProductResponse>> = _productos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchProductos()
    }

    private fun fetchProductos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Llamada a tu EC2 usando mainApiService
                val response = RetrofitClient.mainApiService.getAllProducts()
                if (response.isSuccessful) {
                    _productos.value = response.body() ?: emptyList()
                } else {
                    println("Error backend: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Error conexión: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
