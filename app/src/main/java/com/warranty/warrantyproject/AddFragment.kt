package com.warranty.warrantyproject

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.warranty.warrantyproject.databinding.FragmentAddBinding
import com.warranty.warrantyproject.db.WarrantyDatabase
import com.warranty.warrantyproject.presenters.AddPresenter
import com.warranty.warrantyproject.presenters.views.CanCreateAddView
import com.warranty.warrantyproject.viewmodel.WarrantyViewModel
import com.warranty.warrantyproject.viewmodel.WarrantyViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment(),CanCreateAddView {

    private lateinit var binding : FragmentAddBinding
    private lateinit var presenter : AddPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater,container,false)

        val viewModel = ViewModelProvider(requireActivity(), WarrantyViewModelFactory(WarrantyDatabase.getDatabase(requireContext()).warrantyDao()))[WarrantyViewModel::class.java]

        presenter = AddPresenter(this,viewModel)

        binding.addCompAddScreen.setOnClickListener {
            saveWarranty()
        }

        binding.returnCompAddScreen.setOnClickListener {
            goBackHome()
        }

        val dateOfPurchaseInputLayout = binding.productDateOfPurchase
        val dateOfPurchaseEditText = binding.productDateOfPurchaseEditText

        attachDatePickerToTextInput(dateOfPurchaseInputLayout, dateOfPurchaseEditText, requireContext())

        val maxGuaranteeDateInputLayout = binding.productMaxGuaranteeDate
        val maxGuaranteeDateEditText = binding.productMaxGuaranteeDateEditText

        attachDatePickerToTextInput(maxGuaranteeDateInputLayout, maxGuaranteeDateEditText, requireContext())

        return binding.root

    }

    private fun saveWarranty() {
        val title = binding.productNameText.text.toString()
        val summary = binding.productSummaryText.text.toString()
        val shopName = binding.productShopText.text.toString()
        val price = binding.productPriceValue.text.toString()
        val dateOfPurchase = binding.productDateOfPurchaseEditText.text.toString()
        val dateOfExpiry = binding.productMaxGuaranteeDateEditText.text.toString()
        val imageProofLink = "blabla"
        val imageCoverLink = "toutoutou"
        presenter.saveWarranty(title,summary,shopName,price.toDouble(),Date(dateOfPurchase),Date(dateOfExpiry),imageProofLink,imageCoverLink)
        // Gestion de la notification à ajouter
    }

    private fun goBackHome() {
        view?.findNavController()?.navigate(R.id.action_addFragment_to_homeFragment)
    }
    private fun attachDatePickerToTextInput(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText, context: Context) {
        val datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance().apply {
                set(year, monthOfYear, dayOfMonth)
            }
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            textInputEditText.setText(dateFormat.format(calendar.time))
        }
        textInputEditText.inputType = InputType.TYPE_NULL
        textInputEditText.isClickable = true
        textInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val calendar = Calendar.getInstance()
                DatePickerDialog(context, datePickerDialog, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
        textInputLayout.isHintEnabled = false
    }
}