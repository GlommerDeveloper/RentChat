package com.rent.service

import com.rent.actor.ClientView.{PostMessage, PostMessageToGeneral, clientInCluster}
import com.rent.controller.ChatController
import com.rent.model.{Customer, Message}
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.Initializable
import javafx.geometry.Pos
import javafx.scene.control.{ListCell, ListView}
import javafx.scene.text.{Font, TextAlignment}

import java.net.URL
import java.util.ResourceBundle

class ChatService extends ChatController with Initializable {

    private var myself: Customer = _
    private var currentFriend: Customer = _
    private val generalRoom: Customer = new Customer(200002, "General", null)

    override def initialize(location: URL, resources: ResourceBundle): Unit = {

        sendButton.setVisible(false)
        messagesTextField.setVisible(false)

        friendsListView.setCellFactory((elem: ListView[Customer]) => new ListCell[Customer]() {
            override def updateItem(item: Customer, empty: Boolean): Unit = {
                super.updateItem(item, empty)
                setStyle("-fx-control-inner-background: #02315E; -fx-text-fill: #c6d3fa;")
                setFont(Font.font("Impact",20))
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
                if (currentFriend.equals(myself)){
                    sendButton.setVisible(false)
                    messagesTextField.setVisible(false)
                    chatListView.getItems.clear()
                } else {
                    sendButton.setVisible(true)
                    messagesTextField.setVisible(true)
                    chatListView.getItems.clear()
                    myself.getListMessagesWithFriends(currentFriend).foreach(msg => chatListView.getItems.add(msg))
                }
            }
        })

        chatListView.setCellFactory((elem: ListView[Message]) => new ListCell[Message]() {
            override def updateItem(item: Message, empty: Boolean): Unit = {
                super.updateItem(item, empty)
                setStyle("-fx-control-inner-background: #00457E; -fx-text-fill: #c6d3fa;")
                setFont(Font.font("Impact",16))
                if (empty || item == null) {
                    setText(null)
                } else {
                    setText(item.getTextBody)
                    if (item.getFrom == myself.getRef) {
                        setAlignment(Pos.TOP_RIGHT)
                    } else {
                        setAlignment(Pos.TOP_LEFT)
                    }
                }
            }
        })

        sendButton.setOnMouseEntered(event => {
            sendButton.setStyle("-fx-background-color: #643a7e")
        })

        sendButton.setOnMouseExited(event => {
            sendButton.setStyle("-fx-background-color: #806491")
        })

        sendButton.setOnAction(event => {
            if (messagesTextField.getText.nonEmpty) {
                println("--Button is pressed--")
                val message: Message = new Message(myself.getRef, currentFriend.getRef, messagesTextField.getText)
                messagesTextField.clear()
                myself.saveMessagesInChat(message, currentFriend)
                chatListView.getItems.clear()
                myself.getListMessagesWithFriends(currentFriend).foreach(msg => chatListView.getItems.add(msg))
                if (currentFriend.getPort == 200002){
                    println("---SEND MESSAGE TO ACTOR---")
                    myself.getRef ! PostMessageToGeneral(message, myself, generalRoom)
                } else {
                    currentFriend.getRef ! PostMessage(message, myself)
                }
            }
        })
    }

    def setMySelf(receivedMySelf: Customer): Unit = {
        println("------SET MYSELF------")
        friendsListView.getItems.add(generalRoom)
        friendsListView.getItems.add(receivedMySelf)
        myself = receivedMySelf
    }

    def newUser(client: Customer): Unit = {
        println("------TRYING TO WRITE NEW USER INTO LIST------")
        var check: Boolean = true
        friendsListView.getItems.forEach(friend => if(friend.equals(client)) check = false)
        if (check) {
            friendsListView.getItems.add(client)
        }
    }

    def setMessageFromOtherActor(message: Message, friend: Customer): Unit = {
        myself.saveMessagesInChat(message, friend)
        println("------CURRENT_FRIEND: " + currentFriend + "; FRIEND: " + friend)
        if (currentFriend.equals(friend)) {
            println("--I'M HERE--")
            chatListView.getItems.clear()
            myself.getListMessagesWithFriends(friend).foreach(msg => chatListView.getItems.add(msg))
        }
    }
}
