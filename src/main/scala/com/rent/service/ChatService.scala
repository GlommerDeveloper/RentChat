package com.rent.service

import akka.actor.typed.ActorRef
import com.rent.actor.ClientView.{Event, NewClient}
import com.rent.controller.ChatController
import com.rent.model.{Customer, Message}
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.Initializable
import javafx.scene.control.{ListCell, ListView}
import javafx.util.Callback

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
                chatListView = myself.getMapMessagesWithFriends.apply(currentFriend)
            }
        })

        chatListView.setCellFactory((elem: ListView[Message]) => new ListCell[Message]() {
            override def updateItem(item: Message, empty: Boolean): Unit = {
                super.updateItem(item, empty)
                if (empty || item == null) {
                    setText(null)
                } else {
                    setText(item.getTextBody)
                }
            }
        })

        sendButton.setOnAction(event => {
            if (messagesTextField.getText.nonEmpty) {

            }
        })
    }

    def sayClusterThatImAlive(ref: ActorRef[Event]): Unit ={
        Thread.sleep(3000)
        ref ! NewClient(myPort, myNickname)
    }

    def setPortAndNickname(port: Int, nickname: String): Unit = {
        myPort = port
        myNickname = nickname
    }

    def setMySelf(receivedMySelf: Customer): Unit = {
        println("------SET MYSELF------")
        friendsListView.getItems.add(receivedMySelf)
        myself = receivedMySelf
    }

    def newUser(client: Customer): Unit = {
        println("------TRYING TO WRITE NEW USER INTO LIST------")
        friendsListView.getItems.add(client)
        myself.setMessageToMap(client, new ListView[Message]())
    }
}
