package com.rent.service

import com.rent.RentApplication.system
import com.rent.controller.ChatController
import javafx.fxml.Initializable
import javafx.scene.input.KeyCode
import com.rent.model.Message
import javafx.scene.control.{ListCell, ListView}
import viewChatController.ClientView.PostMessage

import java.net.URL
import java.util.ResourceBundle

class ChatService extends ChatController with Initializable{
    override def initialize(location: URL, resources: ResourceBundle): Unit = {
        sendButton.setOnAction(event => {
            sendMessage()
        })
        messagesTextField.setOnKeyPressed(event => {
            if (event.getCode.equals(KeyCode.ENTER)) {
                sendMessage()
            }
        })
    }

    private def sendMessage(): Unit = {
        if (messagesTextField.getText.isEmpty) {
            println("ERROR")
        } else {
            chatListView.setCellFactory()
            chatListView.getItems.add(new Message("", "", messagesTextField.getText))
            messagesTextField.clear()
        }
        system ! PostMessage(new Message("", "", "Actor"))
    }

    def postMessage(message: Message): Unit = {
        chatListView.getItems.add(message)
    }
}
