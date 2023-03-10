package com.warranty.warrantyproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warranty.warrantyproject.db.WarrantyDao
import com.warranty.warrantyproject.db.WarrantyEntity
import kotlinx.coroutines.launch

// Lorsque le constructeur du viewmodel prend un argument (ou plus), c'est une bonne pratique de construire un viewmodel factory
class WarrantyViewModel(private val warrantyDao: WarrantyDao): ViewModel() {
    val warranties = warrantyDao.getWarranties()

    fun insertWarranty(warrantyEntity: WarrantyEntity) = viewModelScope.launch {
        warrantyDao.insertWarranty(warrantyEntity)
    }

    fun updateWarranty(warrantyEntity: WarrantyEntity) = viewModelScope.launch {
        warrantyDao.updateWarranty(warrantyEntity)
    }

    fun deleteWarranty(warrantyEntity: WarrantyEntity) = viewModelScope.launch {
        warrantyDao.deleteWarranty(warrantyEntity)
    }
}