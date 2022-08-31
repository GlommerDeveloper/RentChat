package controller

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.Button
import javafx.stage.Stage

import java.io.IOException


class HomePageController {

  @FXML
  private var signInButton: Button = _
  @FXML
  private var signUpButton: Button = _

  @FXML
  def initialize(): Unit = {

    signInButton.setOnAction(event => {
      signInButton.getScene.getWindow.hide()
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

    signUpButton.setOnAction(event => {
      signUpButton.getScene.getWindow.hide()
      val loader: FXMLLoader = new FXMLLoader()
      loader.setLocation(getClass.getResource("/signUp.fxml"))
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
