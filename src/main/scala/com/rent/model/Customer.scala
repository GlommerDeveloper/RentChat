package com.rent.model

import akka.actor.typed.ActorRef
import com.rent.actor.ClientView
import com.rent.actor.ClientView.{Event, JSer}


class Customer(constructPort: Int, constructNickName: String, constructRef: ActorRef[ClientView.Event])  extends  JSer{
    private val port: Int = constructPort
    private val nickName: String = constructNickName
    private val refOnActor: ActorRef[ClientView.Event] = constructRef
    private var mapMessagesWithFriends: Map[ActorRef[Event], List[Message]] = Map.empty[ActorRef[Event], List[Message]]

    def getPort: Int = port

    def getNickName: String = nickName

    def getRef: ActorRef[ClientView.Event] = refOnActor

    def getMapMessagesWithFriends: Map[ActorRef[Event], List[Message]] = mapMessagesWithFriends

    def setFriendToMap(friendRef: ActorRef[Event]): Unit = {
        mapMessagesWithFriends = mapMessagesWithFriends + (friendRef ->  List[Message]())
    }

    def setMessageToListInMap(message: Message, fromMe: Boolean): Unit = {
        val list: List[Message] = List[Message](message)
        if(fromMe){
            mapMessagesWithFriends = mapMessagesWithFriends + (message.getTo -> list)
        }else{
            mapMessagesWithFriends = mapMessagesWithFriends + (message.getFrom -> list)
        }
    }

    override def toString: String = {
        port.toString + " " + nickName + " " + refOnActor.toString
    }
}
