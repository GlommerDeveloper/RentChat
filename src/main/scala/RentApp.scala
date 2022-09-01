import akka.actor.TypedActor.context
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, scaladsl}
import akka.cluster.typed.Cluster
import com.typesafe.config.ConfigFactory
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

import java.io.IOException

object RentMain extends App {
  Application.launch(classOf[RentApp])
}

class RentApp extends Application{

  val LocalPort = ConfigFactory.load().getInt("MyPort")

//  object RootBehavior {
//    def apply(): Behavior[Nothing] = Behaviors.setup[Nothing] { ctx =>
//      val cluster = Cluster(ctx.system)
//      context = ctx
//
//
//      if (cluster.selfMember.hasRole("frontend")) {
//        session = ctx.spawn(SessionActor(), "Frontend")
//        Behaviors.empty
//      } else {
//        Behaviors.stopped
//      }
//
//    }
//  }

  def startup(role: String, port: Int): Unit = {
    // Override the configuration of the port and role

    val config = ConfigFactory
        .parseString(
          s"""
        akka.remote.artery.canonical.port=$port
        akka.cluster.roles = [$role]
        """)
        // ¯\_(ツ)_/¯
        .withFallback(ConfigFactory.load(ConfigFactory.load()))
    ActorSystem[Nothing](SessionActor(), "system", config)

  }

  override def start(primaryStage: Stage): Unit = {
    val loader: FXMLLoader = new FXMLLoader()
    loader.setLocation(getClass.getResource("homePage.fxml"))
    try{
      val scene: Scene = new Scene(loader.load(), 1000, 700)
      primaryStage.setScene(scene)
      primaryStage.show()
    } catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }
}
