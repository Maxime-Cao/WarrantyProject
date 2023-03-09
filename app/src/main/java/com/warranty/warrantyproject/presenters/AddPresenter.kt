package com.warranty.warrantyproject.presenters

import com.warranty.warrantyproject.db.WarrantyEntity
import com.warranty.warrantyproject.presenters.views.CanCreateAddView
import com.warranty.warrantyproject.viewmodel.WarrantyViewModel
import java.util.*

class AddPresenter {
    private lateinit var view : CanCreateAddView
    private lateinit var viewModel : WarrantyViewModel

    constructor(view: CanCreateAddView, viewModel: WarrantyViewModel) {
        this.view = view
        this.viewModel = viewModel
    }

    fun saveWarranty(title:String, summary:String, shopName:String, price:Double, dateOfPurchase: Date, dateOfExpiry:Date, imageProofLink:String, imageCoverLink:String) {
        viewModel.insertWarranty(WarrantyEntity(
            0,title,summary, shopName, price, dateOfPurchase, dateOfExpiry, imageProofLink, imageCoverLink
        ))
    }
}