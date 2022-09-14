package com.rent.controller

import javafx.fxml.FXML
import javafx.scene.control.{Button, ListView, TextField}
import com.rent.model.{Customer, Message}


class ChatController {

  @FXML
  protected var chatListView: ListView[Message] = _
  @FXML
  protected var friendsListView: ListView[Customer] = _
  @FXML
  protected var messagesTextField: TextField = _
  @FXML
  protected var sendButton: Button = _

  @FXML
  def initialize(): Unit = {

  }
}
