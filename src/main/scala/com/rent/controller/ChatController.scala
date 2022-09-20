package com.rent.controller

import javafx.fxml.FXML
import javafx.scene.control.{Button, ListView, TextField}
import com.rent.model.{Customer, Message}
import javafx.scene.control


class ChatController {

  @FXML
  protected var chatListView: ListView[Message] = new ListView[Message]()
  @FXML
  protected var friendsListView: ListView[Customer] = new ListView[Customer]()
  @FXML
  protected var messagesTextField: TextField = _
  @FXML
  protected var sendButton: Button = _

  @FXML
  def initialize(): Unit = {

  }
}
