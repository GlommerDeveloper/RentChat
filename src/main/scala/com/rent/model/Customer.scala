package com.rent.model

import akka.actor.typed.ActorRef
import akka.actor.typed.receptionist.ServiceKey
import com.rent.actor.ClientView
import javafx.scene.control.ListView

import scala.collection.mutable

class Customer(constructPort: Int, constructNickName: String, constructRef: ActorRef[ClientView.Event]) extends Serializable{
    private val port: Int = constructPort
    private val nickName: String = constructNickName
    private val refOnActor: ActorRef[ClientView.Event] = constructRef
    private var mapMessagesWithFriends: mutable.Map[Customer, ListView[Message]] = mutable.Map.empty[Customer, ListView[Message]]

    def getPort: Int = port

    def getNickName: String = nickName

    def getRef: ActorRef[ClientView.Event] = refOnActor

    def getMapMessagesWithFriends: mutable.Map[Customer, ListView[Message]] = mapMessagesWithFriends

    def setMessageToMap(friend: Customer, list: ListView[Message]): Unit = {
        mapMessagesWithFriends(friend) = list
    }

    override def toString: String = {
        port.toString + " " + nickName + " " + refOnActor.toString
    }
}
