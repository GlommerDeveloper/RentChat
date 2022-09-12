package viewChatController

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import com.rent.model.{Client, Message}
import com.rent.service.ChatService
import javafx.application.Platform

object ClientView {

    val clientServiceKey = ServiceKey[ClientView.Event]("Client")

    sealed trait Event

    private final case class ClientsUpdated(newClients: Set[ActorRef[ClientView.Event]]) extends Event

    case class NewClient(clientPort: Int, clientNickName: String) extends Event

    case class PostMessage(message: Message) extends Event


    def apply(controller: ChatService): Behavior[Event] = Behaviors.setup { ctx =>

        ctx.system.receptionist ! Receptionist.Register(clientServiceKey, ctx.self)

        val subscriptionAdapter = ctx.messageAdapter[Receptionist.Listing] {
            case ClientView.clientServiceKey.Listing(clients) =>
                ClientsUpdated(clients)
        }

        ctx.system.receptionist ! Receptionist.Subscribe(ClientView.clientServiceKey, subscriptionAdapter)
        running(ctx, IndexedSeq.empty, controller)
    }

    private def running(ctx: ActorContext[Event], clients: IndexedSeq[ActorRef[ClientView.Event]], controller: ChatService): Behavior[Event] =
        Behaviors.receiveMessage {
            case ClientsUpdated(newClients) =>
                println("Size: " + newClients.size)
                ctx.log.info("List of services registered with the receptionist changed: {}", newClients)
                running(ctx, newClients.toIndexedSeq, controller)

            case NewClient(clientPort, clientNickName) =>
                println("IN NEWCLIENT CASE")
                Platform.runLater(() => controller.newUser(clientPort, clientNickName))
                Behaviors.same
        }
}
