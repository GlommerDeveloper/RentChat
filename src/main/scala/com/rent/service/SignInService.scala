package com.rent.service

import akka.actor.typed.Scheduler
import akka.util.Timeout
import com.rent.RentApplication.startup
import com.rent.actor.ClientView
import com.rent.actor.ClientView.NewClient
import com.rent.controller.SignInController
import javafx.fxml.{FXMLLoader, Initializable}
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.io.IOException
import java.net.URL
import java.util.ResourceBundle
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt

class SignInService extends SignInController with Initializable {

    private var userPort: Int = _
    private var userNickName: String = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {

        signInButton.setOnAction(event => {
            if (nickNameField.getText.nonEmpty || portField.getText.nonEmpty) {
                userPort = portField.getText.toInt
                userNickName = nickNameField.getText
                signInButton.getScene.getWindow.hide()

                val system = startup("clientView", userPort)
                implicit val timeout: Timeout = Timeout(20.seconds)
                implicit val scheduler: Scheduler = system.scheduler
                implicit val context: ExecutionContextExecutor = system.executionContext
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
                val receivedController: ChatService = loader.getController
                stage.setScene(new Scene(root))
                stage.show()

                val clientActor = system.systemActorOf(ClientView.apply(controller = receivedController), "myself")
                clientActor ! NewClient(userPort, userNickName)
            }
        })
    }
}
