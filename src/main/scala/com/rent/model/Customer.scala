package com.rent.model

import akka.actor.typed.ActorRef
import com.rent.actor.ClientView
import com.rent.actor.ClientView.JSer


class Customer(constructPort: Int, constructNickName: String, constructRef: ActorRef[ClientView.Event]) extends JSer {
    private val port: Int = constructPort
    private val nickName: String = constructNickName
    private val refOnActor: ActorRef[ClientView.Event] = constructRef
    private var mapMessagesWithFriends: Map[Int, List[Message]] = Map.empty[Int, List[Message]]

    def getPort: Int = port

    def getNickName: String = nickName

    def getRef: ActorRef[ClientView.Event] = refOnActor

    def getListMessagesWithFriends(currentFriend: Customer): List[Message] = {
        if (mapMessagesWithFriends.contains(currentFriend.getPort)) {
            mapMessagesWithFriends.apply(currentFriend.getPort)
        } else {
            List.empty[Message]
        }
    }

    def setNewFriendToMap(friend: Customer): Unit = {
        mapMessagesWithFriends += (friend.getPort -> List())
    }

    def saveMessagesInChat(message: Message, friend: Customer): Unit = {

        if (mapMessagesWithFriends.contains(friend.getPort)) {
            val list: List[Message] = mapMessagesWithFriends.apply(friend.getPort)
            mapMessagesWithFriends = mapMessagesWithFriends.updated(friend.getPort, list :+ message)
        } else {
            mapMessagesWithFriends += (friend.getPort -> List(message))
        }
    }

    override def toString: String = {
        port.toString + " " + nickName + " " + refOnActor.toString
    }

    def canEqual(a: Any) = a.isInstanceOf[Customer]

    override def equals(that: Any): Boolean =
        that match {
            case that: Customer => that.canEqual(this) && this.hashCode == that.hashCode
            case _ => false
        }

    override def hashCode: Int = {
        val prime = 31
        var result = 1
        result = prime * result + port;
        result = prime * result + (if (nickName == null) 0 else nickName.hashCode)
        result
    }
}
