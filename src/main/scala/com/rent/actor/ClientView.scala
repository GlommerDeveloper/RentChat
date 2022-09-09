package viewChatController

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.util.Timeout
import com.rent.service.ChatService
import com.rent.utils.CborSerializable

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object ClientView {

    val ClientServiceKey = ServiceKey[ClientView.Event]("Client")

    sealed trait Event

    private final case class ClientsUpdated(newWorkers: Set[ActorRef[ClientView.Event]]) extends Event

    def apply(controller: ChatService): Behavior[Event] = Behaviors.setup { ctx =>
        ctx.system.receptionist ! Receptionist.Register(ClientServiceKey, ctx.self)

        val subscriptionAdapter = ctx.messageAdapter[Receptionist.Listing] {
            case ClientView.ClientServiceKey.Listing(workers) =>
                ClientsUpdated(workers)
        }

        ctx.system.receptionist ! Receptionist.Subscribe(ClientView.ClientServiceKey, subscriptionAdapter)

        running(ctx, IndexedSeq.empty)
    }

    private def running(ctx: ActorContext[Event], workers: IndexedSeq[ActorRef[ClientView.Event]]): Behavior[Event] =
        Behaviors.receiveMessage {
            case ClientsUpdated(newWorkers) =>
                println("Size: " + newWorkers.size)
                ctx.log.info("List of services registered with the receptionist changed: {}", newWorkers)
                running(ctx, newWorkers.toIndexedSeq)
        }
}
