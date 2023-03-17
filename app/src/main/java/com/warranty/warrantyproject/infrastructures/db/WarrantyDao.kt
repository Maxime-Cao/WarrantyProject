package com.warranty.warrantyproject.infrastructures.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WarrantyDao {
    // suspend permet de dire que la fonction est gérée de manière asynchrone par une coroutine séparée du thread principal

    // Pour ce type de requête, pas besoin de mettre le mot-clé suspend car c'est gérer par une coroutine spéciale, directement grâce à la librairie Room
    @Query("SELECT * FROM warranty_data_table")
    fun getWarranties(): LiveData<List<WarrantyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWarranty(warrantyEntity: WarrantyEntity) : Long

    @Update
    suspend fun updateWarranty(warrantyEntity: WarrantyEntity)

    @Delete
    suspend fun deleteWarranty(warrantyEntity: WarrantyEntity)
}