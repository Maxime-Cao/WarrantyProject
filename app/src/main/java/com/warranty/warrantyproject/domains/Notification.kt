package com.warranty.warrantyproject.domains

class Notification {
    var isActive : Boolean
    var dateOfNotification : NotificationPeriod

    constructor(isActive: Boolean, dateOfNotification: NotificationPeriod){
        this.isActive = isActive
        this.dateOfNotification = dateOfNotification
    }
}