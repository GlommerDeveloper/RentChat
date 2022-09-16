package com.rent.service

import com.rent.actor.ClientView.PostMessage
import com.rent.controller.ChatController
import com.rent.model.{Customer, Message}
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.Initializable
import javafx.scene.control.{ListCell, ListView, SelectionMode}
import javafx.scene.text.TextAlignment

import java.net.URL
import java.util.ResourceBundle

class ChatService extends ChatController with Initializable {

    private var myself: Customer = _
    private var currentFriend: Customer = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {

        sendButton.setVisible(false)
        messagesTextField.setVisible(false)

        friendsListView.setCellFactory((elem: ListView[Customer]) => new ListCell[Customer]() {
            override def updateItem(item: Customer, empty: Boolean): Unit = {
                super.updateItem(item, empty)
                if (empty || item == null) {
                    setText(null)
                } else {
                    setText(item.getNickName)
                }
            }
        })

        friendsListView.getSelectionModel.selectedItemProperty().addListener(new ChangeListener[Customer] {
            override def changed(observable: ObservableValue[_ <: Customer], oldValue: Customer, newValue: Customer): Unit = {
                if(currentFriend == myself){
                    sendButton.setVisible(false)
                    messagesTextField.setVisible(false)
                } else {
                    currentFriend = friendsListView.getSelectionModel.getSelectedItem
                    sendButton.setVisible(true)
                    messagesTextField.setVisible(true)
                    chatListView.getItems.clear()
                    myself.getMapMessagesWithFriends(currentFriend.getPort).foreach(msg => chatListView.getItems.add(msg))
                }
            }
        })

        chatListView.setCellFactory((elem: ListView[Message]) => new ListCell[Message]() {
            override def updateItem(item: Message, empty: Boolean): Unit = {
                super.updateItem(item, empty)
                if (empty || item == null) {
                    setText(null)
                } else {
                    setText(item.getTextBody)
                    if(item.getFrom == myself.getRef){
                        setTextAlignment(TextAlignment.RIGHT)
                    }else{
                        setTextAlignment(TextAlignment.LEFT)
                    }
                }
            }
        })

        sendButton.setOnAction(event => {
            if (messagesTextField.getText.nonEmpty) {
                println("--Button is pressed--")
                val message: Message = new Message(myself.getRef, currentFriend.getRef, messagesTextField.getText)
                messagesTextField.clear()
                myself.saveMessagesInChat(message, currentFriend)
                chatListView.getItems.clear()
                chatListView.getItems.addAll(myself.getMapMessagesWithFriends(currentFriend.getPort):_*)
                currentFriend.getRef ! PostMessage(message, myself)
            }
        })
    }

    def setMySelf(receivedMySelf: Customer): Unit = {
        println("------SET MYSELF------")
        friendsListView.getItems.add(receivedMySelf)
        myself = receivedMySelf
    }

    def newUser(client: Customer): Unit = {
        println("------TRYING TO WRITE NEW USER INTO LIST------")
        friendsListView.getItems.add(client)
    }

    def setMessageFromOtherActor(message: Message, friend: Customer): Unit = {
        myself.saveMessagesInChat(message, friend)
    }
}
