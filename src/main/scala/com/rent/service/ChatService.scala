package com.rent.service

import com.rent.controller.ChatController
import com.rent.model.Customer
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.Initializable
import javafx.scene.control.{ListCell, ListView}
import javafx.util.Callback

import java.net.URL
import java.util.ResourceBundle

class ChatService extends ChatController with Initializable{
    override def initialize(location: URL, resources: ResourceBundle): Unit = {

        friendsListView.setCellFactory((elem: ListView[Customer]) => new ListCell[Customer](){
            override def updateItem(item: Customer, empty: Boolean): Unit = {
                super.updateItem(item, empty)
                if(empty || item == null){
                    setText(null)
                } else {
                    setText(item.getNickName)
                }
            }
        })

        friendsListView.getSelectionModel.selectedItemProperty().addListener(new ChangeListener[Customer] {
            override def changed(observable: ObservableValue[_ <: Customer], oldValue: Customer, newValue: Customer): Unit = {
                var currentClient: Customer = friendsListView.getSelectionModel.getSelectedItem
                println(currentClient)
            }
        })
    }

    def newUser(client: Customer): Unit = {
        println("I'M TRYING WRITE NEW USER TO LIST")
        friendsListView.getItems.add(client)
    }
}
