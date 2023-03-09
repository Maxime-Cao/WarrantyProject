package com.warranty.warrantyproject.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "warranty_data_table")
data class WarrantyEntity(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "warranty_id") var id: Int,
                          @ColumnInfo(name = "warranty_title") var title: String,
                          @ColumnInfo(name = "warranty_summary") var summary: String,
                          @ColumnInfo(name = "warranty_shop_name") var shopName: String,
                          @ColumnInfo(name = "warranty_price") var price: Double,
                          @ColumnInfo(name = "warranty_date_purchase") var dateOfPurchase: Date,
                          @ColumnInfo(name = "warranty_date_expiry") var dateOfExpiry: Date,
                          @ColumnInfo(name = "warranty_proof_image_link") var imageProofLink: String,
                          @ColumnInfo(name = "warranty_cover_image_link") var imageCoverLink: String)