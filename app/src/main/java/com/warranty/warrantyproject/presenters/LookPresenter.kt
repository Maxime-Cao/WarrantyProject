package com.warranty.warrantyproject.presenters

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.warranty.warrantyproject.NotificationScheduler
import com.warranty.warrantyproject.domains.Notification
import com.warranty.warrantyproject.domains.NotificationPeriodSelector
import com.warranty.warrantyproject.infrastructures.db.WarrantyEntity
import com.warranty.warrantyproject.presenters.views.CanCreateLookView
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class LookPresenter {
    private lateinit var view : CanCreateLookView
    private lateinit var viewModel : WarrantyViewModel
    private lateinit var notificationPeriodSelector: NotificationPeriodSelector
    private lateinit var notificationScheduler: NotificationScheduler

    constructor(view: CanCreateLookView,viewModel: WarrantyViewModel) {
        this.view = view
        this.viewModel = viewModel
        this.notificationPeriodSelector = NotificationPeriodSelector()
        this.notificationScheduler = NotificationScheduler(view.getCurrentContext())
        notificationScheduler.createNotificationChannel()
    }

    fun getWarranty(requireActivity: FragmentActivity): WarrantyEntity {
        return viewModel.currentWarranty;
    }

    fun saveWarranty(id : Long,title: String, summary: String, shopName: String, price: Double, dateOfPurchase: Date?, dateOfExpiry: Date, imageProofLink: String, imageCoverLink: String, requireNotification: Boolean, notificationChoice: String?) {
        val imageProofLinkToDelete = viewModel.currentWarranty.imageProofLink
        val imageCoverLinkToDelete = viewModel.currentWarranty.imageCoverLink
        viewModel.updateWarranty(WarrantyEntity(id, title, summary, shopName, price, dateOfPurchase, dateOfExpiry, imageProofLink, imageCoverLink))
        if(imageProofLinkToDelete != imageProofLink) {
            Log.d("1deleteFile", "deleteFile: $imageProofLinkToDelete")
            viewModel.deleteFile(imageProofLinkToDelete,view.getCurrentContext())
        }
        if(imageCoverLinkToDelete != imageCoverLink) {
            Log.d("2deleteFile", "deleteFile: $imageCoverLinkToDelete")
            viewModel.deleteFile(imageCoverLinkToDelete,view.getCurrentContext())
        }
        if (requireNotification) {
            updateNotification(id,notificationChoice,dateOfExpiry,title)
        }
    }
    private fun updateNotification(id : Long,notificationChoice: String?, dateOfExpiry: Date, title: String) {
        GlobalScope.launch {
            if(notificationScheduler.checkIfNotificationExists(id)){
                notificationScheduler.cancelNotification(id.toInt())
            }
            addNotification(id,notificationChoice,dateOfExpiry,title)
        }
    }

    private fun addNotification(id: Long, notificationChoice: String?,dateOfExpiry: Date,title: String) {
        GlobalScope.launch {
            val periodChoice = notificationPeriodSelector.getNotificationPeriods(notificationChoice)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
            val formattedExpiryDate = dateFormat.format(dateOfExpiry)

            if(periodChoice != -1) {
                val calendar = Calendar.getInstance()
                calendar.time = dateOfExpiry
                calendar.add(Calendar.DAY_OF_MONTH,-(periodChoice))
                calendar.set(Calendar.HOUR_OF_DAY, 11)
                notificationScheduler.scheduleNotification(
                    id.toInt(),
                    "Your $title product warranty expires on $formattedExpiryDate",
                    calendar.timeInMillis
                )
            }
        }
    }

    fun getNotificationPeriods(endOfWarrantyDate: String)  {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val diff = dateFormat.parse(endOfWarrantyDate)!!.time  - Date().time
        val periods : List<String>

        if(diff <= 0) {
            periods = notificationPeriodSelector.getNotificationPeriods(0)
        } else {
            periods = notificationPeriodSelector.getNotificationPeriods((diff/(24*3600*1000)).toInt())
        }

        view.setPeriods(periods)
    }

    fun getNotification(id: Long, dateOfExpiry: Date) : Notification? {
        return notificationScheduler.getNotification(id, dateOfExpiry)
    }

    fun deleteWarranty(id: Long, context: Context?) {
        viewModel.deleteWarranty(id, context)
        notificationScheduler.cancelNotification(id.toInt())
    }
}