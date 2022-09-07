package com.rent.controller

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, PasswordField, TextField}
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.io.IOException


class SignUpController {
  @FXML
  private var confirmPasswordField: PasswordField = _
  @FXML
  private var loginField: TextField = _
  @FXML
  private var passwordField: PasswordField = _
  @FXML
  private var returnButton: Button = _
  @FXML
  private var signUpButton: Button = _

  @FXML
  def initialize(): Unit = {
    returnButton.setOnAction(event => {
      returnButton.getScene.getWindow.hide()
      val loader: FXMLLoader = new FXMLLoader()
      loader.setLocation(getClass.getResource("/homePage.fxml"))
      try{
        loader.load()
      } catch {
        case exception: IOException =>
          exception.printStackTrace()
      }
      val root: Parent = loader.getRoot()
      val stage: Stage = new Stage()
      stage.setScene(new Scene(root))
      stage.show()
    })

    signUpButton.setOnAction(event => {
      signUpButton.getScene.getWindow.hide()
      val loader: FXMLLoader = new FXMLLoader()
      loader.setLocation(getClass.getResource("/signIn.fxml"))
      try{
        loader.load()
      } catch {
        case exception: IOException =>
          exception.printStackTrace()
      }
      val root: Parent = loader.getRoot()
      val stage: Stage = new Stage()
      stage.setScene(new Scene(root))
      stage.show()
    })
  }
}
