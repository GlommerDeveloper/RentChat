package com.rent.service

import akka.actor.typed.Scheduler
import akka.util.Timeout
import com.rent.RentApp.startup
import com.rent.actor.ClientView
import com.rent.actor.ClientView.NewClient
import com.rent.controller.SignInController
import javafx.fxml.{FXMLLoader, Initializable}
import javafx.scene.image.Image
import javafx.scene.input.{KeyCode, KeyEvent}
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.io.IOException
import java.net.{ServerSocket, URL}
import java.util.ResourceBundle
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt
import scala.util.Using

class SignInService extends SignInController with Initializable {

    private val userPort: Int = freePorts
    private var userNickName: String = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {

        portField.setText(userPort.toString)

        signInButton.setOnMouseEntered(_ => {
            signInButton.setStyle("-fx-background-color: #643a7e")
        })

        signInButton.setOnMouseExited(_ => {
            signInButton.setStyle("-fx-background-color: #806491")
        })

        signInButton.setOnAction(_ => {
            signIn()
        })

        nickNameField.setOnKeyPressed((t: KeyEvent) => {
            if(t.getCode.equals(KeyCode.ENTER)) signIn()
        })

        portField.setOnKeyPressed((t: KeyEvent) => {
            if(t.getCode.equals(KeyCode.ENTER)) signIn()
        })
    }

    def signIn(): Unit = {
        if (nickNameField.getText.nonEmpty || portField.getText.nonEmpty) {
            userNickName = nickNameField.getText
            signInButton.getScene.getWindow.hide()

            val system = startup(userPort)                          //RUN CLUSTER
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
            val root: Parent = loader.getRoot
            val stage: Stage = new Stage()
            val receivedController: ChatService = loader.getController
            stage.setScene(new Scene(root))
            stage.getIcons.add(new Image("icon.png"))
            stage.setTitle("Chat")
            stage.show()

            val clientActor = system.systemActorOf(ClientView.apply(controller = receivedController), "myself")
            clientActor ! NewClient(userPort, userNickName)
        }
    }

    def freePorts: Int = {
        var freePort: Int = 0
        try{
            new ServerSocket(25251).close()
            freePort = 25251
            freePort
        } catch {
            case _: IOException =>
                Using(new ServerSocket(0)) (_.getLocalPort).foreach(port => freePort = port)
                freePort
        }
    }
}
