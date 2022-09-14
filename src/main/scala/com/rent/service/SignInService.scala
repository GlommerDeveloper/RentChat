package com.rent.service

import akka.actor.typed.receptionist.Receptionist
import com.rent.RentApplication.{clientActor, startup}
import com.rent.actor.ClientView.NewClient
import com.rent.controller.SignInController
import javafx.fxml.{FXMLLoader, Initializable}
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.io.IOException
import java.net.URL
import java.util.ResourceBundle

class SignInService extends SignInController with Initializable{

    private var userPort: Int = _
    private var userNickName: String = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {

        signInButton.setOnAction(event => {
            if (nickNameField.getText.nonEmpty || portField.getText.nonEmpty) {
                userPort = portField.getText.toInt
                userNickName = nickNameField.getText

                signInButton.getScene.getWindow.hide()
                val loader: FXMLLoader = new FXMLLoader()
                loader.setLocation(getClass.getResource("/chat.fxml"))
                try {
                    loader.load()
                } catch {
                    case exception: IOException =>
                        exception.printStackTrace()
                }
                val root: Parent = loader.getRoot()
                val stage: Stage = new Stage()
                startup("clientView", userPort, loader.getController)
                stage.setScene(new Scene(root))
                stage.show()
            }
        })
    }

    def getUserPort: Int = userPort
    def getUserNickname: String = userNickName
}
