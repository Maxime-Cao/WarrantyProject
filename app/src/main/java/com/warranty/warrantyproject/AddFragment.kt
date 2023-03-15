package com.warranty.warrantyproject

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.warranty.warrantyproject.databinding.FragmentAddBinding
import com.warranty.warrantyproject.infrastructures.db.WarrantyDatabase
import com.warranty.warrantyproject.presenters.AddPresenter
import com.warranty.warrantyproject.presenters.views.CanCreateAddView
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModel
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModelFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AddFragment : Fragment(),CanCreateAddView {

    private lateinit var binding : FragmentAddBinding

    private lateinit var presenter : AddPresenter

    private var selectedButton : Button? = null

    private var undefinedUri : Uri? = null

    private var productImageUri: String = ""
    private var warrantyProofUri : String = ""

    private val warrantyProofFolder : String = "warranty_proof_images"
    private val productImagesFolder : String = "product_images"

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri ? ->
        uri?.let {
            val inputStream = context?.contentResolver?.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val drawable = BitmapDrawable(resources, bitmap)
            selectedButton?.foreground = drawable
            handleUri(uri)
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
        if (isSuccessful) {
            undefinedUri?.let {
                val inputStream = context?.contentResolver?.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val drawable = BitmapDrawable(resources, bitmap)
                selectedButton?.foreground = drawable
                handleUri(it)
            }
        }
    }

    private val requestReadStoragePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGranted ->
        if(isGranted) {
            pickImage.launch("image/*")
        } else {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)) {
                displayInfoDialog("Permission required","The following permission is required to add image to your warranty : READ_EXTERNAL_STORAGE. You have chosen to decline this permission by selecting the \"don't ask again\" option. If you want to use this feature, please allow this permission using your phone settings.")
            }
        }
    }

    private val requestNotificationPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        permissions ->
        val permissionsGranted = permissions.filterValues { it }
        val permissionsDeniedWithDontAskAgain = permissions.filterKeys { key -> !ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),key) }

        if(permissions.size != permissionsGranted.size) {
            binding.notificationBoolean.isChecked = false
           if(permissionsDeniedWithDontAskAgain.isNotEmpty()) {
               if(permissionsDeniedWithDontAskAgain.size == 1) {
                   displayInfoDialog("Permission required","The following permission is required to set up a notification for your warranty: ${arrayOf(permissionsDeniedWithDontAskAgain.keys).contentToString()}. You have chosen to decline this permission by selecting the \"don't ask again\" option. If you want to use this feature, please allow this permission using your phone settings.")
               } else {
                   displayInfoDialog("Permissions required","The following permissions are required to set up a notification for your warranty: ${arrayOf(permissionsDeniedWithDontAskAgain.keys).contentToString()}. You have chosen to deny these permissions by selecting the \"don't ask again\" option. If you want to use this feature, please allow these permissions using your phone settings.")
               }
           }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater,container,false)

        val viewModel = ViewModelProvider(requireActivity(), WarrantyViewModelFactory(
            WarrantyDatabase.getDatabase(requireContext()).warrantyDao())
        )[WarrantyViewModel::class.java]

        presenter = AddPresenter(this,viewModel)

        binding.notificationBoolean.setOnClickListener {
            if(binding.notificationBoolean.isChecked) {
                requestNotificationAccess()
            }
        }

        binding.addCompAddScreen.setOnClickListener {
            saveWarranty()
        }

        binding.returnCompAddScreen.setOnClickListener {
            goBackHome()
        }

        binding.productMaxGuaranteeDateEditText.addTextChangedListener {
            updateSpinner()
        }

        binding.buttonAddImage.setOnClickListener {
            selectedButton = binding.buttonAddImage
            displayImagePickerDialog()
        }

        binding.warrantyButton.setOnClickListener {
            selectedButton = binding.warrantyButton
            displayImagePickerDialog()
        }

        configureDatePicker()


        return binding.root
    }

    private fun handleUri(uri: Uri) {
        if(selectedButton == binding.buttonAddImage) {
            this.productImageUri = uri.toString()
        } else if(selectedButton == binding.warrantyButton) {
            this.warrantyProofUri = uri.toString()

        }
    }

    private fun displayImagePickerDialog() {
        val picker = AlertDialog.Builder(requireContext())
        val choices = arrayOf<CharSequence>("Take a picture","Open gallery","Cancel")
        picker.setTitle("Select an option")
        picker.setItems(choices) {dialog,choice ->
            when (choices[choice]) {
                "Take a picture" -> openCamera()
                "Open gallery" -> openGallery()
                "Cancel" -> dialog.dismiss()
            }
        }
        picker.show()
    }

    private fun displayInfoDialog(title:String,message:String) {
        var dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setNeutralButton("Ok") {
                dial, _ -> dial.dismiss()
        }
        dialog.show()
    }

    private fun openCamera() {
        undefinedUri = createImageUri() ?: return
        takePicture.launch(undefinedUri)
    }

    private fun openGallery() {
        requestReadMediaAccess()
    }

    private fun createImageUri() : Uri? {
        val storageDirectory  = when (selectedButton) {
            binding.buttonAddImage -> File(requireContext().filesDir, productImagesFolder)
            binding.warrantyButton -> File(requireContext().filesDir, warrantyProofFolder)
            else -> null
        }

       if(storageDirectory != null) {
           if(!storageDirectory.exists()) {
               storageDirectory.mkdirs()
           }
           val image = File(storageDirectory, "WarrantyApp_${Calendar.getInstance().timeInMillis}.png")
           return FileProvider.getUriForFile(requireContext(),"${context?.packageName}.provider",image)
       }
        return null
    }

    private fun configureDatePicker() {
        val dateOfPurchaseInputLayout = binding.productDateOfPurchase
        val dateOfPurchaseEditText = binding.productDateOfPurchaseEditText

        attachDatePickerToTextInput(dateOfPurchaseInputLayout, dateOfPurchaseEditText, requireContext())

        val maxGuaranteeDateInputLayout = binding.productMaxGuaranteeDate
        val maxGuaranteeDateEditText = binding.productMaxGuaranteeDateEditText

        attachDatePickerToTextInput(maxGuaranteeDateInputLayout, maxGuaranteeDateEditText, requireContext())
    }

    override fun getCurrentContext(): Context? {
        return context
    }

    private fun updateSpinner() {
        val endOfWarrantyDate = binding.productMaxGuaranteeDateEditText.text.toString().trim()

        if(endOfWarrantyDate.isNotEmpty()) {
            presenter.getNotificationPeriods(endOfWarrantyDate)
        }

    }

    private fun requestReadMediaAccess() {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestReadStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                pickImage.launch("image/*")
            }
        } else {
            pickImage.launch("image/*")
        }
    }

    private fun requestNotificationAccess() {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
                }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.SCHEDULE_EXACT_ALARM
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.SCHEDULE_EXACT_ALARM)
            }
        }
        if(permissions.isNotEmpty()) {
            requestNotificationPermission.launch(permissions.toTypedArray())
        }
    }

    override fun setPeriods(periods: List<String>) {
        binding.notificationTime.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, periods)
    }

    private fun saveWarranty() {
        val titleField = binding.productNameText
        val priceField = binding.productPriceValue
        val dateOfPurchaseField = binding.productDateOfPurchaseEditText
        val dateOfExpiryField = binding.productMaxGuaranteeDateEditText

        val shopNameText = binding.productShopText.text.toString().trim()
        val summaryText = binding.productSummaryText.text.toString().trim()
        val notificationBoolean = binding.notificationBoolean.isChecked
        var notificationTime = binding.notificationTime.selectedItem?.toString()
        Log.d("Coucou",productImageUri)

        if(validateFields(titleField,priceField,dateOfPurchaseField,dateOfExpiryField,warrantyProofUri,notificationBoolean)) {
            val dateOfPurchaseText = dateOfPurchaseField.text.toString()
            val dateOfExpiryText = dateOfExpiryField.text.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
            val dateOfPurchaseDateFormat = if(dateOfPurchaseText.isEmpty()) null else dateFormat.parse(dateOfPurchaseText)

            dateFormat.parse(dateOfExpiryText)?.let {
                presenter.saveWarranty(
                    titleField.text.toString().trim(),
                    summaryText,
                    shopNameText,
                    priceField.text.toString().toDouble(),
                    dateOfPurchaseDateFormat,
                    it,
                    warrantyProofUri,
                    productImageUri,
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


    private fun removeErrors() {
        binding.productNameText.error = null
        binding.productPriceValue.error = null
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
        val dateFormat = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
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
        if(imageProofLink.isEmpty()) {
            binding.warrantyButton.error = "Image required"
            return false
        }
        return imageProofLink.isNotEmpty()
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