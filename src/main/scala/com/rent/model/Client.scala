package com.rent.model

import akka.actor.typed.receptionist.ServiceKey
import viewChatController.ClientView

import scala.:+

class Client(constructPort: Int, constructNickName: String, constructKey: ServiceKey[ClientView.Event]) {
    private val port: Int = constructPort
    private val nickName: String = constructNickName
    private val key: ServiceKey[ClientView.Event] = constructKey

    def getPort: Int = port

    def getNickName: String = nickName

    def getKey: ServiceKey[ClientView.Event] = key

    override def toString: String = {
        port.toString + " : " + nickName + " : " + key.toString
    }
}
