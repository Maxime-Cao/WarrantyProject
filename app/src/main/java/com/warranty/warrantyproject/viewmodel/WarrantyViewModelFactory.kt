package com.warranty.warrantyproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.warranty.warrantyproject.db.WarrantyDao

class WarrantyViewModelFactory (private val warrantyDao: WarrantyDao):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WarrantyViewModel::class.java)) {
            return WarrantyViewModel(warrantyDao) as T
        }
        throw java.lang.IllegalArgumentException("Unknown view model class")
    }
}