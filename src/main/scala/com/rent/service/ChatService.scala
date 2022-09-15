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
    private var myPort: Int = _
    private var myNickname: String = _


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
                currentFriend = friendsListView.getSelectionModel.getSelectedItem
                sendButton.setVisible(true)
                messagesTextField.setVisible(true)
                chatListView.getItems.clear()
                chatListView.getItems.addAll(myself.getMapMessagesWithFriends(currentFriend.getRef):_*)
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

        chatListView.getSelectionModel.setSelectionMode(SelectionMode.MULTIPLE)

        sendButton.setOnAction(event => {
            if (messagesTextField.getText.nonEmpty) {
                println("--Button is pressed--")
                val message: Message = new Message(myself.getRef, currentFriend.getRef, messagesTextField.getText)
                chatListView.getItems.add(message)
                messagesTextField.clear()
                currentFriend.getRef ! PostMessage(message)
            }
        })
    }

    def setMySelf(receivedMySelf: Customer): Unit = {
        println("------SET MYSELF------")
        friendsListView.getItems.add(receivedMySelf)
        myself = receivedMySelf
        myself.setFriendToMap(myself.getRef)
    }

    def newUser(client: Customer): Unit = {
        println("------TRYING TO WRITE NEW USER INTO LIST------")
        friendsListView.getItems.add(client)
        myself.setFriendToMap(client.getRef)
    }

    def setPortAndNickname(receivedPort: Int, receivedNickname: String): Unit ={
        myPort = receivedPort
        myNickname = receivedNickname
    }

    def setMessageFromOtherActor(message: Message): Unit = {
        val fromMe: Boolean = if(message.getFrom == myself.getRef) true else false
        myself.setMessageToListInMap(message, fromMe)
    }
//
//    def getMyPort: Int = myPort
//
//    def getMyNickname: String = myNickname
}
