package controller

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import model.{Client, Message}


class ChatController {

  @FXML
  private var chatListView: ListView[Message] = _
  @FXML
  private var friendsListView: ListView[Client] = _
  @FXML
  private var messagesTextField: TextField = _
  @FXML
  private var sendButton: Button = _

  @FXML
  def initialize(): Unit = {

  }
}
