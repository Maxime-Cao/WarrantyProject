package com.warranty.warrantyproject.domains

enum class NotificationPeriod(val day: String, val value: Int) {
    ONE_WEEK("One week", 7),
    TWO_WEEKS("Two weeks",14),
    ONE_MONTH("One month",30),
    TWO_MONTHS("Two months",60),
    THREE_MONTHS("Three months",90),
    SIX_MONTHS("Six months",180),
    ONE_YEAR("One year",365)
}