package com.warranty.warrantyproject.domains

import org.junit.Before
import org.junit.Test
import java.util.Date

class NotificationPeriodSelectorTest {
    private lateinit var notificationPeriodSelector: NotificationPeriodSelector
    @Before
    fun setUp() {
        notificationPeriodSelector = NotificationPeriodSelector()
    }
    @Test
    fun getNotificationPeriods0Days() {
        val daysBetween = 0
        val result = notificationPeriodSelector.getNotificationPeriods(daysBetween)
        assert(result.size == 1)
        assert(result[0] == "D-day")
    }

    @Test
    fun getNotificationPeriods1Days() {
        val daysBetween = 1
        val result = notificationPeriodSelector.getNotificationPeriods(daysBetween)
        assert(result.size == 2)
        assert(result[0] == "D-day")
        assert(result[1] == "One day")
    }

    @Test
    fun getNotificationPeriods1Week() {
        val daysBetween = 7
        val result = notificationPeriodSelector.getNotificationPeriods(daysBetween)
        assert(result.size == 3)
        assert(result[0] == "D-day")
        assert(result[1] == "One day")
        assert(result[2] == "One week")
    }

    @Test
    fun getNotificationPeriods2Weeks() {
        val daysBetween = 14
        val result = notificationPeriodSelector.getNotificationPeriods(daysBetween)
        assert(result.size == 4)
        assert(result[0] == "D-day")
        assert(result[1] == "One day")
        assert(result[2] == "One week")
        assert(result[3] == "Two weeks")
    }

    @Test
    fun getNotificationPeriods1Month() {
        val daysBetween = 30
        val result = notificationPeriodSelector.getNotificationPeriods(daysBetween)
        assert(result.size == 5)
        assert(result[0] == "D-day")
        assert(result[1] == "One day")
        assert(result[2] == "One week")
        assert(result[3] == "Two weeks")
        assert(result[4] == "One month")
    }

    @Test
    fun getNotificationPeriods2Months() {
        val daysBetween = 60
        val result = notificationPeriodSelector.getNotificationPeriods(daysBetween)
        assert(result.size == 6)
        assert(result[0] == "D-day")
        assert(result[1] == "One day")
        assert(result[2] == "One week")
        assert(result[3] == "Two weeks")
        assert(result[4] == "One month")
        assert(result[5] == "Two months")
    }
    @Test
    fun getNotificationPeriods3Months() {
        val daysBetween = 90
        val result = notificationPeriodSelector.getNotificationPeriods(daysBetween)
        assert(result.size == 7)
        assert(result[0] == "D-day")
        assert(result[1] == "One day")
        assert(result[2] == "One week")
        assert(result[3] == "Two weeks")
        assert(result[4] == "One month")
        assert(result[5] == "Two months")
        assert(result[6] == "Three months")
    }

    @Test
    fun getNotificationPeriods6Months() {
        val daysBetween = 180
        val result = notificationPeriodSelector.getNotificationPeriods(daysBetween)
        assert(result.size == 8)
        assert(result[0] == "D-day")
        assert(result[1] == "One day")
        assert(result[2] == "One week")
        assert(result[3] == "Two weeks")
        assert(result[4] == "One month")
        assert(result[5] == "Two months")
        assert(result[6] == "Three months")
        assert(result[7] == "Six months")
    }

    @Test
    fun getNotificationPeriods1Year() {
        val daysBetween = 365
        val result = notificationPeriodSelector.getNotificationPeriods(daysBetween)
        assert(result.size == 9)
        assert(result[0] == "D-day")
        assert(result[1] == "One day")
        assert(result[2] == "One week")
        assert(result[3] == "Two weeks")
        assert(result[4] == "One month")
        assert(result[5] == "Two months")
        assert(result[6] == "Three months")
        assert(result[7] == "Six months")
        assert(result[8] == "One year")
    }

    @Test
    fun getNotificationPeriodsNegative() {
        val daysBetween = -1
        val result = notificationPeriodSelector.getNotificationPeriods(daysBetween)
        assert(result.size == 0)
    }

    @Test
    fun getNotificationPeriodsKey0Days() {
        val dayKey = "D-day"
        val result = notificationPeriodSelector.getNotificationPeriods(dayKey)
        assert(result == 0)
    }

    @Test
    fun getNotificationPeriodsKey1Days() {
        val dayKey = "One day"
        val result = notificationPeriodSelector.getNotificationPeriods(dayKey)
        assert(result == 1)
    }

    @Test
    fun getNotificationPeriodsKey1Week() {
        val dayKey = "One week"
        val result = notificationPeriodSelector.getNotificationPeriods(dayKey)
        assert(result == 7)
    }

    @Test
    fun getNotificationPeriodsKey2Weeks() {
        val dayKey = "Two weeks"
        val result = notificationPeriodSelector.getNotificationPeriods(dayKey)
        assert(result == 14)
    }

    @Test
    fun getNotificationPeriodsKey1Month() {
        val dayKey = "One month"
        val result = notificationPeriodSelector.getNotificationPeriods(dayKey)
        assert(result == 30)
    }

    @Test
    fun getNotificationPeriodsKey2Months() {
        val dayKey = "Two months"
        val result = notificationPeriodSelector.getNotificationPeriods(dayKey)
        assert(result == 60)
    }

    @Test
    fun getNotificationPeriodsKey3Months() {
        val dayKey = "Three months"
        val result = notificationPeriodSelector.getNotificationPeriods(dayKey)
        assert(result == 90)
    }

    @Test
    fun getNotificationPeriodsKey6Months() {
        val dayKey = "Six months"
        val result = notificationPeriodSelector.getNotificationPeriods(dayKey)
        assert(result == 180)
    }

    @Test
    fun getNotificationPeriodsKey1Year() {
        val dayKey = "One year"
        val result = notificationPeriodSelector.getNotificationPeriods(dayKey)
        assert(result == 365)
    }

    @Test
    fun getSpinnerPosition0Days() {
        val notificationPeriod = Date(2023, 3, 18)
        val dateOfExpiry = Date(2023, 3, 18)
        val result = notificationPeriodSelector.getSpinnerPosition(notificationPeriod, dateOfExpiry)
        assert(result == 0)
    }

    @Test
    fun getSpinnerPosition1Days() {
        val notificationPeriod = Date(2023, 3, 18)
        val dateOfExpiry = Date(2023, 3, 19)
        val result = notificationPeriodSelector.getSpinnerPosition(notificationPeriod, dateOfExpiry)
        assert(result == 0)
    }

    @Test
    fun getSpinnerPosition1Week() {
        val notificationPeriod = Date(2023, 3, 18)
        val dateOfExpiry = Date(2023, 3, 25)
        val result = notificationPeriodSelector.getSpinnerPosition(notificationPeriod, dateOfExpiry)
        assert(result == 2)
    }
}