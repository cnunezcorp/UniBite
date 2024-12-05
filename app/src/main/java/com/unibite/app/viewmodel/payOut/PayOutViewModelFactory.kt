package com.unibite.app.viewmodel.payOut

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unibite.app.domain.usecase.OrderUseCase


class PayOutViewModelFactory(private val useCase: OrderUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PayOutViewModel::class.java)) {
            return PayOutViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}