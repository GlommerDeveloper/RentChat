package controller

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.stage.Stage

import java.io.IOException


class SignInController {
  @FXML
  private var signInButton: Button = _
  @FXML
  private var loginField: TextField = _
  @FXML
  private var passwordField: PasswordField = _
  @FXML
  private var returnButton: Button = _

  @FXML
  def initialize(): Unit = {
    signInButton.setOnAction(event => {
      signInButton.getScene.getWindow.hide()
      val loader: FXMLLoader = new FXMLLoader()
      loader.setLocation(getClass.getResource("/chat.fxml"))
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
  }
}
