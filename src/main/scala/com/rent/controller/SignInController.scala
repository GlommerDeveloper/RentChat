package com.rent.controller

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, PasswordField, TextField}
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.io.IOException


class SignInController {
  @FXML
  protected var signInButton: Button = _
  @FXML
  protected var loginField: TextField = _
  @FXML
  protected  var portField: TextField = _

  @FXML
  def initialize(): Unit = {

  }
}
