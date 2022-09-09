package viewChatController

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import com.rent.model.Message
import com.rent.service.ChatService

object ClientView {

    val ClientServiceKey = ServiceKey[ClientView.Event]("Client")

    sealed trait Event

    private final case class ClientsUpdated(newWorkers: Set[ActorRef[ClientView.Event]]) extends Event

    case class PostMessage(message: Message) extends Event

    def apply(controller: ChatService): Behavior[Event] = Behaviors.setup { ctx =>

        ctx.system.receptionist ! Receptionist.Register(ClientServiceKey, ctx.self)

        val subscriptionAdapter = ctx.messageAdapter[Receptionist.Listing] {
            case ClientView.ClientServiceKey.Listing(clients) =>
                ClientsUpdated(clients)
        }

        ctx.system.receptionist ! Receptionist.Subscribe(ClientView.ClientServiceKey, subscriptionAdapter)
        running(ctx, IndexedSeq.empty, controller)
    }

    private def running(ctx: ActorContext[Event], clients: IndexedSeq[ActorRef[ClientView.Event]], controller: ChatService): Behavior[Event] =
        Behaviors.receiveMessage {
            case ClientsUpdated(newClients) =>
                println("Size: " + newClients.size)
                ctx.log.info("List of services registered with the receptionist changed: {}", newClients)
                running(ctx, newClients.toIndexedSeq, controller)

//            case PostMessage() =>
//                controller.postMessage()
        }
}
