import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.IOException

object RentMain extends App {
  Application.launch(classOf[RentApp])
}

class RentApp extends Application{
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
