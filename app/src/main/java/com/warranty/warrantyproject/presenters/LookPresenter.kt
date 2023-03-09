package com.warranty.warrantyproject.presenters

import com.warranty.warrantyproject.presenters.views.CanCreateLookView
import com.warranty.warrantyproject.viewmodel.WarrantyViewModel

class LookPresenter {
    private lateinit var view : CanCreateLookView
    private lateinit var viewModel : WarrantyViewModel

    constructor(view: CanCreateLookView,viewModel: WarrantyViewModel) {
        this.view = view
        this.viewModel = viewModel
    }
}