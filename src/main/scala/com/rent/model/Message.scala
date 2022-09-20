package com.rent.model

import akka.actor.typed.ActorRef
import com.rent.actor.ClientView.{Event, JSer}

class Message(outerFrom: ActorRef[Event], outerTo: ActorRef[Event], outerTextBody: String) extends JSer{
    private var from: ActorRef[Event] = outerFrom
    private var to: ActorRef[Event] = outerTo
    private var textBody: String = outerTextBody


    def getFrom: ActorRef[Event] = this.from
    def setFrom(from: ActorRef[Event]): Unit = {
        this.from = from
    }

    def getTo: ActorRef[Event] = this.to

    def setTo(to: ActorRef[Event]) = {
        this.to = to
    }

    def getTextBody: String = this.textBody

    def setTextBody(textBody: String): Unit = {
        this.textBody = textBody
    }
}
