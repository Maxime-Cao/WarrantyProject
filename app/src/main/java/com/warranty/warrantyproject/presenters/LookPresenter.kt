package com.warranty.warrantyproject.presenters

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

    fun saveWarranty(id: Long,title: String, summary: String, shopName: String, price: Double, dateOfPurchase: Date?, dateOfExpiry: Date, imageProofLink: String, imageCoverLink: String, requireNotification: Boolean, notificationChoice: String?) {
        viewModel.updateWarranty(WarrantyEntity(id, title, summary, shopName, price, dateOfPurchase, dateOfExpiry, imageProofLink, imageCoverLink))
        if (requireNotification) {
            updateNotification(notificationChoice,dateOfExpiry,title)
        }
    }
    private fun updateNotification(notificationChoice: String?, dateOfExpiry: Date, title: String) {
        GlobalScope.launch {
            val periodChoice = notificationPeriodSelector.getNotificationPeriods(notificationChoice)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedExpiryDate = dateFormat.format(dateOfExpiry)

            //Est-ce que la notification existe déjà ?
            val notificationExists = notificationScheduler.checkIfNotificationExists(viewModel.currentWarranty.id)
            if(notificationExists) {
                //Si oui, on l'update
                notificationScheduler.updateNotification(viewModel.currentWarranty.id,"Your $title product warranty expires on: $dateOfExpiry",dateOfExpiry.time)
            } else {
                //Si non, on la crée
                //Faire un Deferred<Long> avec : viewModel.currentWarranty.id
                val generatedId : Deferred<Long> = GlobalScope.async {
                    viewModel.currentWarranty.id
                }
                addNotification(generatedId,notificationChoice,dateOfExpiry,title)
            }
        }

    }

    private fun addNotification(generatedIdDeferred: Deferred<Long>, notificationChoice: String?, dateOfExpiry: Date, title: String) {
        GlobalScope.launch {
            val generatedId = generatedIdDeferred.await()
            val periodChoice = notificationPeriodSelector.getNotificationPeriods(notificationChoice)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedExpiryDate = dateFormat.format(dateOfExpiry)

            if(periodChoice != -1) {
                val calendar = Calendar.getInstance()
                calendar.time = dateOfExpiry
                calendar.add(Calendar.DAY_OF_MONTH,-(periodChoice))
                calendar.set(Calendar.HOUR_OF_DAY, 11)
                notificationScheduler.scheduleNotification(generatedId.toInt(),"Your $title product warranty expires on: $formattedExpiryDate",calendar.timeInMillis)
                // Uniquement pour tester la notif :  notificationScheduler.scheduleNotification(generatedId.toInt(),"Your $title product warranty expires on $formattedExpiryDate",dateEnMillisecondesQueTuVeux)
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
}