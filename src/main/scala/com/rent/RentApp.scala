package com.rent

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.cluster.typed.Cluster
import com.rent.actor.ClientView
import com.rent.actor.ClientView.{Event, JSer}
import com.typesafe.config.ConfigFactory
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

import java.io.IOException


object RentApplication {

    var clientActor: ActorRef[ClientView.Event] = _

    trait RootCmd extends JSer

    object RootBehavior {
        def apply(): Behavior[RootCmd] = Behaviors.setup[RootCmd] { ctx =>
            val cluster = Cluster(ctx.system)
            Behaviors.empty
        }
    }

    def startup(role: String, port: Int): ActorSystem[RootCmd] = {

        val config = ConfigFactory
            .parseString(
                s"""
              akka.remote.artery.canonical.port=$port
              akka.cluster.roles = [$role]
              """)
            .withFallback(ConfigFactory.load())

        ActorSystem(RootBehavior(), "ClusterSystem", config)
    }

    def main(args: Array[String]): Unit = {
        Application.launch(classOf[RentApp])
    }
}

class RentApp extends Application {

    override def start(primaryStage: Stage): Unit = {
        val loader: FXMLLoader = new FXMLLoader()
        loader.setLocation(getClass.getResource("/signIn.fxml"))
        try {
            val scene: Scene = new Scene(loader.load(), 1000, 700)
            primaryStage.setScene(scene)
            primaryStage.setResizable(false)
            primaryStage.getIcons.add(new Image("icon.png"))
            primaryStage.setTitle("Authorization")
            primaryStage.show()
        } catch {
            case e: IOException =>
                e.printStackTrace()
        }
    }
}
