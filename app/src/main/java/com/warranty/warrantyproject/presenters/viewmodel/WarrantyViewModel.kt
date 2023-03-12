package com.warranty.warrantyproject.presenters.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warranty.warrantyproject.domains.Warranty
import com.warranty.warrantyproject.infrastructures.db.WarrantyDao
import com.warranty.warrantyproject.infrastructures.db.WarrantyEntity
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// Lorsque le constructeur du viewmodel prend un argument (ou plus), c'est une bonne pratique de construire un viewmodel factory
class WarrantyViewModel(private val warrantyDao: WarrantyDao): ViewModel() {
    lateinit var currentWarranty: WarrantyEntity
    val warranties = warrantyDao.getWarranties()


    fun insertWarranty(warrantyEntity: WarrantyEntity): Deferred<Long> = viewModelScope.async {
        warrantyDao.insertWarranty(warrantyEntity)
    }

    fun updateWarranty(warrantyEntity: WarrantyEntity) = viewModelScope.launch {
        Log.d("WarrantyViewModel", "updateWarranty: ${warrantyEntity.id}")
        warrantyDao.updateWarranty(warrantyEntity)
    }

    fun deleteWarranty(warrantyEntity: WarrantyEntity) = viewModelScope.launch {
        warrantyDao.deleteWarranty(warrantyEntity)
    }

    fun setCurrentWarranty(warranty: Int) {
        currentWarranty = warranties.value!![warranty]
    }
}