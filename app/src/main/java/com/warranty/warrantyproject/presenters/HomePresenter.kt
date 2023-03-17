package com.warranty.warrantyproject.presenters

import androidx.lifecycle.LifecycleOwner
import com.warranty.warrantyproject.WarrantyAdapter
import com.warranty.warrantyproject.infrastructures.db.WarrantyEntity
import com.warranty.warrantyproject.presenters.views.CanCreateHomeView
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModel

class HomePresenter {
    private lateinit var view : CanCreateHomeView
    private lateinit var viewModel : WarrantyViewModel

    constructor(view: CanCreateHomeView,viewModel: WarrantyViewModel) {
        this.view = view
        this.viewModel = viewModel
    }

    fun getWarrantyList(owner:LifecycleOwner,adapter: WarrantyAdapter) {
        viewModel.warranties.observe(owner) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    fun setCurrentWarranty(warranty: Int) {
        viewModel.setCurrentWarranty(warranty)
    }

    fun getWarranties(): List<WarrantyEntity> {
        return viewModel.warranties.value!!
    }

    fun getView(): CanCreateHomeView {
        return view
    }

    fun getViewModel() : WarrantyViewModel {
        return viewModel
    }

}