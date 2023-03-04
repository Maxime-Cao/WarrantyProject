package com.warranty.warrantyproject

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.warranty.warrantyproject.databinding.FragmentAddBinding
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment() {

    private lateinit var binding : FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater,container,false)

        binding.addCompAddScreen.setOnClickListener {
            goBackHome(it)
        }

        binding.returnCompAddScreen.setOnClickListener {
            goBackHome(it)
        }

        val dateOfPurchaseInputLayout = binding.productDateOfPurchase
        val dateOfPurchaseEditText = binding.productDateOfPurchaseEditText

        // Attacher un sélecteur de date à dateOfPurchaseInputLayout et dateOfPurchaseEditText
        attachDatePickerToTextInput(dateOfPurchaseInputLayout, dateOfPurchaseEditText, requireContext())

        // Trouver les TextInputLayout et TextInputEditText pour la date de garantie maximale
        val maxGuaranteeDateInputLayout = binding.productMaxGuaranteeDate
        val maxGuaranteeDateEditText = binding.productMaxGuaranteeDateEditText

        // Attacher un sélecteur de date à maxGuaranteeDateInputLayout et maxGuaranteeDateEditText
        attachDatePickerToTextInput(maxGuaranteeDateInputLayout, maxGuaranteeDateEditText, requireContext())

        return binding.root

    }

    fun goBackHome(view:View) {
        view.findNavController().navigate(R.id.action_addFragment_to_homeFragment)
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
}