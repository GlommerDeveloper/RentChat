package com.rent

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}
import akka.cluster.typed.Cluster
import com.rent.RentApplication.startup
import com.rent.service.ChatService
import com.typesafe.config.ConfigFactory
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import viewChatController.ClientView

import java.io.IOException


object RentApplication {
    object RootBehavior {
        def apply(): Behavior[Nothing] = Behaviors.setup[Nothing] { ctx =>
            val cluster = Cluster(ctx.system)

//            if (cluster.selfMember.hasRole("backend")) {
//                val workersPerNode =
//                    ctx.system.settings.config.getInt("transformation.workers-per-node")
//                (1 to workersPerNode).foreach { n =>
//                    ctx.spawn(ClientView(), s"Worker$n")
//                    println("SPAWN WORKER")
//                }
//            }
            if (cluster.selfMember.hasRole("frontend")) {
                ctx.spawn(ClientView(getChatController), "Frontend")
                println("SPAWN FRONTEND")
            }
            Behaviors.empty
        }
    }

    def startup(role: String, port: Int): Unit = {
        // Override the configuration of the port and role
        val config = ConfigFactory
            .parseString(s"""
              akka.remote.artery.canonical.port=$port
              akka.cluster.roles = [$role]
              """)
            .withFallback(ConfigFactory.load())

        ActorSystem[Nothing](RootBehavior(), "ClusterSystem", config)

    }
     def main(args: Array[String]): Unit = {
        Application.launch(classOf[RentApp])
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

    var localPort: Int = _ // Определяется при вводе в поле "Порт" при авторизации
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
    }
}
