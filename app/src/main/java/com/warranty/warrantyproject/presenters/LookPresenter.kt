package com.warranty.warrantyproject.presenters

import android.util.Log
import com.warranty.warrantyproject.presenters.views.CanCreateLookView
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModel

class LookPresenter {
    private lateinit var view : CanCreateLookView
    private lateinit var viewModel : WarrantyViewModel

    constructor(view: CanCreateLookView,viewModel: WarrantyViewModel) {
        this.view = view
        this.viewModel = viewModel
    }
}