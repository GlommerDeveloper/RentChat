package com.rent

import akka.actor.typed.ActorSystem
import com.rent.RentApplication.system
import com.rent.service.ChatService
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import viewChatController.ClientView

import java.io.IOException


object RentApplication extends App {
    Application.launch(classOf[RentApp])
    var localPort: String = _ // Определяется при вводе в поле "Порт" при авторизации
    var system: ActorSystem[ClientView.Command] = _
}

class RentApp extends Application {

    override def start(primaryStage: Stage): Unit = {
        val loader: FXMLLoader = new FXMLLoader()
        loader.setLocation(getClass.getResource("/signIn.fxml"))
        try {
            val scene: Scene = new Scene(loader.load(), 1000, 700)
            primaryStage.setScene(scene)
            primaryStage.show()
        } catch {
            case e: IOException =>
                e.printStackTrace()
        }

        var gottenController: ChatService = getChatController
        system = ActorSystem(ClientView.apply(gottenController), "clientActor")

    }

    def getChatController: ChatService = {
        val loader: FXMLLoader = new FXMLLoader()
        loader.setLocation(getClass.getResource("/chat.fxml"))
        try {
            loader.load()
        } catch {
            case e: IOException =>
                e.printStackTrace()
        }
        loader.getController
    }


}
