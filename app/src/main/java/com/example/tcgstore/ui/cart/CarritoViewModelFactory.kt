package com.example.tcgstore.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tcgstore.data.UserStorage

class CarritoViewModelFactory(private val userStorage: UserStorage) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarritoViewModel(userStorage) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
