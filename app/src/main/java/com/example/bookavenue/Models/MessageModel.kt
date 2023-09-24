package com.example.bookavenue.Models

class MessageModel{
        var messageId:String?=null
    var message: String? = null
    var senderId: String? = null
    var timeStamp: Long = 0
    var imageUrls:String?=null
var isRead:Boolean?=false
        constructor(){}
    constructor(
        message: String?,
        senderId: String?,
        timeStamp: Long
    ){
        this.message=message
        this.senderId=senderId
        this.timeStamp=timeStamp
    }
}

