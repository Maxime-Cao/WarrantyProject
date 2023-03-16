package com.warranty.warrantyproject.domains

import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class NotificationPeriodSelector {
    fun getNotificationPeriods(daysBetween : Int) : List<String> {
        val periods = ArrayList<String>()
        for(value in NotificationPeriod.values()) {
            if(daysBetween >= value.value) {
                periods.add(value.day)
            }
        }
        return periods
    }

    fun getNotificationPeriods(dayKey:String?) : Int {
        var period = -1
        for(value in NotificationPeriod.values()) {
            if(value.day == dayKey) {
                period = value.value
            }
        }
        return period
    }

    fun getSpinnerPosition(notificationPeriod: Date, dateOfExpiry : Date): Int {
        val diff = dateOfExpiry.time - notificationPeriod.time
        if(diff < 0) {
            return 0
        }
        for((index, value) in NotificationPeriod.values().withIndex()) {
            val calendar = Calendar.getInstance()
            calendar.time = dateOfExpiry
            calendar.add(Calendar.DAY_OF_MONTH,-(value.value))
            calendar.set(Calendar.HOUR_OF_DAY, 11)
            val notificationDate = calendar.time

            if(notificationDate.time == notificationPeriod.time) {

                return index
            }
        }
        return 0
    }
}