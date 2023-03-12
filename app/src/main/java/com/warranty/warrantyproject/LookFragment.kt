package com.warranty.warrantyproject

import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.warranty.warrantyproject.databinding.FragmentLookBinding
import com.warranty.warrantyproject.infrastructures.db.WarrantyDatabase
import com.warranty.warrantyproject.presenters.LookPresenter
import com.warranty.warrantyproject.presenters.views.CanCreateLookView
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModel
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModelFactory
import java.util.*

class LookFragment : Fragment(),CanCreateLookView {
    private lateinit var binding : FragmentLookBinding
    private lateinit var presenter : LookPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookBinding.inflate(inflater,container,false)

        val viewModel = ViewModelProvider(requireActivity(), WarrantyViewModelFactory(
            WarrantyDatabase.getDatabase(requireContext()).warrantyDao())
        )[WarrantyViewModel::class.java]

        presenter = LookPresenter(this,viewModel)

        binding.returnCompLookScreen.setOnClickListener {
            goBackMenu(it)
        }

        binding.validateButton.setOnClickListener {
            saveWarranty()
        }

        binding.deleteCompLookScreen.setOnClickListener {
            goBackMenu(it)
        }

        binding.productMaxGuaranteeDateEditText.addTextChangedListener {
            updateSpinner()
        }

        val dateOfPurchaseInputLayout = binding.productDateOfPurchase
        val dateOfPurchaseEditText = binding.productDateOfPurchaseEditText

        attachDatePickerToTextInput(dateOfPurchaseInputLayout, dateOfPurchaseEditText, requireContext())

        val maxGuaranteeDateInputLayout = binding.productMaxGuaranteeDate
        val maxGuaranteeDateEditText = binding.productMaxGuaranteeDateEditText

        attachDatePickerToTextInput(maxGuaranteeDateInputLayout, maxGuaranteeDateEditText, requireContext())

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val warranty = presenter.getWarranty(requireActivity())
        binding.productNameEditText.setText(warranty.title)
        binding.productPriceEditText.setText(warranty.price.toString())
        binding.productShopEditText.setText(warranty.shopName)
        binding.productSummaryEditText.setText(warranty.summary)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        if(warranty.dateOfPurchase != null) {
            binding.productDateOfPurchaseEditText.setText(dateFormat.format(warranty.dateOfPurchase))
        }
        if(warranty.dateOfExpiry != null) {
            binding.productMaxGuaranteeDateEditText.setText(dateFormat.format(warranty.dateOfExpiry))
        }
    }

    private fun saveWarranty() {
        val titleField = binding.productNameEditText
        val priceField = binding.productPriceEditText
        val dateOfPurchaseField = binding.productDateOfPurchaseEditText
        val dateOfExpiryField = binding.productMaxGuaranteeDateEditText
        val shopNameText = binding.productShopEditText.text.toString().trim()
        val summaryText = binding.productSummaryEditText.text.toString().trim()
        val imageProofLink = "link"
        val imageCoverLink = "link"

        val notificationBoolean = binding.notificationBoolean.isChecked
        val notificationTime = binding.notificationTime.selectedItem?.toString()

        if(validateFields(titleField,priceField,dateOfPurchaseField,dateOfExpiryField,imageProofLink,notificationBoolean)) {
            val dateOfPurchaseText = dateOfPurchaseField.text.toString()
            val dateOfExpiryText = dateOfExpiryField.text.toString()
            val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateOfPurchaseDateFormat = if(dateOfPurchaseText.isEmpty()) null else dateFormat.parse(dateOfPurchaseText)

            dateFormat.parse(dateOfExpiryText)?.let {
                presenter.saveWarranty(
                    presenter.getWarranty(requireActivity()).id,
                    titleField.text.toString().trim(),
                    summaryText,
                    shopNameText,
                    priceField.text.toString().toDouble(),
                    dateOfPurchaseDateFormat,
                    it,
                    imageProofLink,
                    imageCoverLink,
                    notificationBoolean,
                    notificationTime
                )
                goBackHome()
            }
        }
    }

    private fun validateFields(titleField: TextInputEditText, priceField: TextInputEditText, dateOfPurchaseField: TextInputEditText, dateOfExpiryField: TextInputEditText, imageProofLink: String, notificationBoolean: Boolean): Boolean {
        removeErrors()
        return validateTitle(titleField) && validatePrice(priceField) && validateDates(dateOfPurchaseField,dateOfExpiryField,notificationBoolean) && validateImageProofLink(imageProofLink)
    }

    fun goBackMenu(view:View) {
        view.findNavController().navigate(R.id.action_lookFragment_to_homeFragment)
    }

    fun attachDatePickerToTextInput(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText, context: Context) {
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
    private fun updateSpinner() {
        val endOfWarrantyDate = binding.productMaxGuaranteeDateEditText.text.toString().trim()

        if(endOfWarrantyDate.isNotEmpty()) {
            presenter.getNotificationPeriods(endOfWarrantyDate)
        }

    }
    private fun removeErrors() {
        binding.productNameEditText.error = null
        binding.productPriceEditText.error = null
        binding.productDateOfPurchaseEditText.error = null
        binding.productMaxGuaranteeDateEditText.error = null
    }
    private fun validateTitle(titleField: TextInputEditText): Boolean {
        val titleText = titleField.text.toString().trim()
        if(titleText.isEmpty()) {
            titleField.error = "Title required"
            return false
        }
        return true
    }

    private fun validatePrice(priceField : TextInputEditText) : Boolean {
        val priceText = priceField.text.toString().trim()
        if(priceText.isEmpty()) {
            priceField.error = "Price required"
            return false
        }
        return true
    }

    private fun validateDates(dateOfPurchaseField: TextInputEditText, dateOfExpiryField: TextInputEditText, notificationBoolean: Boolean): Boolean {
        val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateOfPurchaseText = dateOfPurchaseField.text.toString().trim()
        val dateOfExpiryText = dateOfExpiryField.text.toString().trim()
        val currentDateText = dateFormat.format(Date())

        if(notificationBoolean && dateOfPurchaseText.isEmpty()) {
            dateOfPurchaseField.error = "Date required"
            return false
        }

        if(dateOfPurchaseText.isNotEmpty() ) {
            if(dateFormat.parse(dateOfPurchaseText)!!.after(dateFormat.parse(currentDateText)) || dateFormat.parse(dateOfPurchaseText)!!.after(dateFormat.parse(dateOfExpiryText))) {
                dateOfPurchaseField.error = "Incorrect date"
                return false
            }
        }

        if(dateOfExpiryText.isEmpty()) {
            dateOfExpiryField.error = "Date required"
            return false
        }

        if(dateFormat.parse(dateOfExpiryText)!!.before(dateFormat.parse(currentDateText))) {
            dateOfExpiryField.error = "Incorrect date"
            return false
        }

        return true
    }

    private fun validateImageProofLink(imageProofLink: String): Boolean {
        // A modifier
        return imageProofLink.isNotEmpty()
    }
    private fun goBackHome() {
        view?.findNavController()?.navigate(R.id.action_lookFragment_to_homeFragment)
    }

    /*override fun setPeriods(periods: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, periods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.notificationTime.adapter = adapter
    }*/
    override fun setPeriods(periods: List<String>) {
        binding.notificationTime.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, periods)
    }

    override fun getCurrentContext(): Context? {
        return context
    }

}