package com.rent.model

import akka.actor.typed.ActorRef
import com.rent.actor.ClientView
import com.rent.actor.ClientView.JSer

import scala.collection.immutable.ArraySeq


class Customer(constructPort: Int, constructNickName: String, constructRef: ActorRef[ClientView.Event])  extends  JSer{
    private val port: Int = constructPort
    private val nickName: String = constructNickName
    private val refOnActor: ActorRef[ClientView.Event] = constructRef
    private var mapMessagesWithFriends: Map[Int, List[Message]] = Map.empty[Int, List[Message]]

    def getPort: Int = port

    def getNickName: String = nickName

    def getRef: ActorRef[ClientView.Event] = refOnActor

    def getMapMessagesWithFriends: Map[Int, List[Message]] = mapMessagesWithFriends

    def saveMessagesInChat(message: Message, friend: Customer): Unit = {

        if(message.getTo == friend.getRef){
            var list: List[Message] = mapMessagesWithFriends.getOrElse(friend.getPort, List.empty[Message])
            list = list :+ message
            mapMessagesWithFriends = mapMessagesWithFriends :+ (friend.getPort -> list)
        } else {
            var list: List[Message] = mapMessagesWithFriends.getOrElse(port, List.empty)
            list = list :+ message
            mapMessagesWithFriends = mapMessagesWithFriends.updated(port, list)
        }
    }

    override def toString: String = {
        port.toString + " " + nickName + " " + refOnActor.toString
    }
}
