package com.warranty.warrantyproject.presenters

import com.warranty.warrantyproject.presenters.views.CanCreateHomeView
import com.warranty.warrantyproject.viewmodel.WarrantyViewModel

class HomePresenter {
    private lateinit var view : CanCreateHomeView
    private lateinit var viewModel : WarrantyViewModel

    constructor(view: CanCreateHomeView,viewModel: WarrantyViewModel) {
        this.view = view
        this.viewModel = viewModel
    }
}