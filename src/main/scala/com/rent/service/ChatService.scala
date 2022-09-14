package com.rent.service

import akka.stream.Client
import com.rent.controller.ChatController
import com.rent.model.Client
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.Initializable
import javafx.scene.control.ListCell
import javafx.util.Callback

import java.net.URL
import java.util.ResourceBundle
import javax.swing.text.html.ListView

class ChatService extends ChatController with Initializable{
    override def initialize(location: URL, resources: ResourceBundle): Unit = {

        friendsListView.setCellFactory((list: ListView[Client]) => new ListCell[Client]{
            override def updateItem(item: Any, empty: Boolean): Unit = {
                super.updateItem(item, empty)

            }
        })

        friendsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener[Client] {
            override def changed(observable: ObservableValue[_ <: Client], oldValue: Client, newValue: Client): Unit = {
                var currentClient: Client = friendsListView.getSelectionModel.getSelectedItem
                println(currentClient)
            }
        })
    }

    def newUser(client: Client): Unit = {
        println("I'M TRYING WRITE NEW USER TO LIST")
        friendsListView.getItems.add(client)
    }
}
