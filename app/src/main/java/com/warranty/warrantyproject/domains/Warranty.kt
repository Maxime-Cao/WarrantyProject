package com.warranty.warrantyproject.domains

import java.util.Date

class Warranty{
    var price: Int
    var dateOfPurchase: Date
    var dateOfExpiry: Date
    var imageWarrantyLink: String
    var notification : Notification
    var warrantyCover: WarrantyCover

    constructor(price: Int, dateOfPurchase: Date, dateOfExpiry: Date, imageWarrantyLink: String, notification: Notification, warrantyCover: WarrantyCover){
        this.price = price
        this.dateOfPurchase = dateOfPurchase
        this.dateOfExpiry = dateOfExpiry
        this.imageWarrantyLink = imageWarrantyLink
        this.notification = notification
        this.warrantyCover = warrantyCover
    }
}