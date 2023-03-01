package com.warranty.warrantyproject.domains

class WarrantyCover{
    var imageLink: String
    var title: String
    var summary: String
    var shopName: String

    constructor(imageLink: String, title: String, summary: String, shopName: String){
        this.imageLink = imageLink
        this.title = title
        this.summary = summary
        this.shopName = shopName
    }
}