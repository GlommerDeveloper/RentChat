package com.rent.service

import com.rent.controller.ChatController
import com.rent.model.{Customer, Message}
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.Initializable
import javafx.scene.control.{ListCell, ListView}

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
        chatListView = new ListView()
        friendsListView.getSelectionModel.selectedItemProperty().addListener(new ChangeListener[Customer] {
            override def changed(observable: ObservableValue[_ <: Customer], oldValue: Customer, newValue: Customer): Unit = {
                currentFriend = friendsListView.getSelectionModel.getSelectedItem
                sendButton.setVisible(true)
                messagesTextField.setVisible(true)
                chatListView.getItems.clear()
                chatListView.getItems.addAll(myself.getMapMessagesWithFriends(currentFriend.getPort):_*)
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

    def setMySelf(receivedMySelf: Customer): Unit = {
        println("------SET MYSELF------")
        friendsListView.getItems.add(receivedMySelf)
        myself = receivedMySelf
        myself.setFriendWithChatToMap(myself.getPort)
    }

    def newUser(client: Customer): Unit = {
        println("------TRYING TO WRITE NEW USER INTO LIST------")
        friendsListView.getItems.add(client)
        myself.setFriendWithChatToMap(client.getPort)
    }

    def setPortAndNickname(receivedPort: Int, receivedNickname: String): Unit ={
        myPort = receivedPort
        myNickname = receivedNickname
    }

    def getMyPort: Int = myPort

    def getMyNickname: String = myNickname
}
