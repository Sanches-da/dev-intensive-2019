package ru.skillbranch.devintensive.models

import java.util.*

abstract class BaseMessage (
     val id:String,
     val from:User?,
     val chat:Chat,
     val isIncoming:Boolean = false,
     val date:Date = Date()
){
    abstract fun formatMessage():String
    companion object AbstractFactory{
        var lastId = -1
        fun makeMessage(from: User?, chat: Chat, date: Date = Date(), type: MessageType=MessageType.TEXT, payload:Any?):BaseMessage{
            lastId++

            return when (type){
                MessageType.TEXT->ImageMessage("$lastId", from, chat, date = date, image = payload as String)
                MessageType.IMAGE->TextMessage("$lastId", from, chat, date = date,  text= payload as String)
            }
        }
    }
}

enum class MessageType{
    TEXT,
    IMAGE
}