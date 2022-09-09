package com.rent.service

import com.rent.RentApplication.{localPort, startup}
import com.rent.controller.SignInController
import javafx.fxml.{FXMLLoader, Initializable}
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.io.IOException
import java.net.URL
import java.util.ResourceBundle

class SignInService extends SignInController with Initializable{


    override def initialize(location: URL, resources: ResourceBundle): Unit = {

        signInButton.setOnAction(event => {
            if(loginField.getText.nonEmpty || portField.getText.nonEmpty) {
                localPort = portField.getText.toInt
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
                stage.setScene(new Scene(root))
                stage.show()
                startup("clientView", localPort)
            }
        })

    }
}
