package com.warranty.warrantyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.lifecycle.ViewModelProvider
import com.warranty.warrantyproject.databinding.ActivityMainBinding
import com.warranty.warrantyproject.infrastructures.db.WarrantyDatabase
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModel
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: WarrantyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,
            WarrantyViewModelFactory(WarrantyDatabase.getDatabase(application).warrantyDao())
        )[WarrantyViewModel::class.java]
    }
}