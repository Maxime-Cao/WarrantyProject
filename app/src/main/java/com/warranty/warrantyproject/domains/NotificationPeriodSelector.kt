package com.warranty.warrantyproject.domains

class NotificationPeriodSelector {
    // Interface à prévoir ?
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
            if(value.day.equals(dayKey)) {
                period = value.value
            }
        }
        return period
    }
}