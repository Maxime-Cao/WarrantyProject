package com.warranty.warrantyproject.domains

import java.util.Date

class Notification {
    var isActive : Boolean
    var dateOfNotification : NotificationPeriod

    constructor(isActive: Boolean, dateOfNotification: NotificationPeriod){
        this.isActive = isActive
        this.dateOfNotification = dateOfNotification
    }
}