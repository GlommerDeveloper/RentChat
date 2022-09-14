package com.rent.model

import akka.actor.typed.ActorRef
import akka.actor.typed.receptionist.ServiceKey
import com.rent.actor.ClientView

class Client(constructPort: Int, constructNickName: String, constructRef: ActorRef[ClientView.Event]) extends Serializable{
    private val port: Int = constructPort
    private val nickName: String = constructNickName
    private val refOnActor: ActorRef[ClientView.Event] = constructRef

    def getPort: Int = port

    def getNickName: String = nickName

    def getRef: ActorRef[ClientView.Event] = refOnActor

    override def toString: String = {
        port.toString + " " + nickName + " " + refOnActor.toString
    }
}
