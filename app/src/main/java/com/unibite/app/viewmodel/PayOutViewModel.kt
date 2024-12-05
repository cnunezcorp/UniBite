package com.unibite.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibite.app.domain.usecase.OrderUseCase
import com.unibite.app.model.OrderDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PayOutViewModel(private val useCase: OrderUseCase) : ViewModel() {

    fun placeOrder(orderDetails: OrderDetails, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                useCase.placeOrder(orderDetails)
                useCase.addOrderToHistory(orderDetails.userUid!!, orderDetails)
                useCase.removeCartItems(orderDetails.userUid!!)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }
}