package com.rent.model

class Message(outerFrom: String, outerTo: String, outerTextBody: String) {
    private var from: String = outerFrom
    private var to: String = outerTo
    private var textBody: String = outerTextBody


    def getFrom: String = this.from
    def setFrom(from: String): Unit = {
        this.from = from
    }

    def getTo: String = this.to

    def setTo(to: String) = {
        this.to = to
    }

    def getTextBody: String = this.textBody

    def setTextBody(textBody: String): Unit = {
        this.textBody = textBody
    }

    override def toString: String = {
        from + to + textBody
    }
}
