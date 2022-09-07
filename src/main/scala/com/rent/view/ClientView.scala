package viewChatController

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.rent.controller.ChatController
import javafx.application.Platform
import com.rent.model.Message
import com.rent.service.ChatService

object ClientView {
    trait Command
    case class PostMessage(newMessage: Message) extends Command

    def apply(controller: ChatService): Behavior[Command] = Behaviors.receive{(context, message) =>
        message match {
            case PostMessage(newMessage) => {
                Platform.runLater(() =>
                    controller.postMessage(newMessage)
                )
                Behaviors.same
            }
            case _ => {
                println("OOPS... Something went wrong")
                Behaviors.same
            }
        }
    }

}
