package viewChatController

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import com.rent.utils.CborSerializable

import scala.concurrent.duration.DurationInt

object ClientView {

    val ClientServiceKey = ServiceKey[ClientView.Event]("ClientView")

    sealed trait Event

    private case object Tick extends Event

    private final case class WorkersUpdated(newWorkers: Set[ActorRef[ClientView.Event]]) extends Event

    private final case class TransformCompleted(originalText: String, transformedText: String) extends Event

    private final case class JobFailed(why: String, text: String) extends Event

    //final case class TransformText(text: String, replyTo: ActorRef[TextTransformed]) extends Event with CborSerializable

    //final case class TextTransformed(text: String) extends CborSerializable

    def apply(): Behavior[Event] = Behaviors.setup { ctx =>

        ctx.log.info("Registering myself with receptionist")

        //Behaviors.withTimers { timers =>
            // subscribe to available workers
            val subscriptionAdapter = ctx.messageAdapter[Receptionist.Listing] {
                case ClientView.ClientServiceKey.Listing(workers) =>
                    WorkersUpdated(workers)
            }
        ctx.system.receptionist ! Receptionist.Register(ClientServiceKey, ctx.self)
        ctx.system.receptionist ! Receptionist.Subscribe(ClientView.ClientServiceKey, subscriptionAdapter)

        //timers.startTimerWithFixedDelay(Tick, Tick, 2.seconds)

        //}
        running(ctx, IndexedSeq.empty, jobCounter = 0)
    }

    private def running(ctx: ActorContext[Event], workers: IndexedSeq[ActorRef[ClientView.Event]], jobCounter: Int): Behavior[Event] =
    Behaviors.receiveMessage {
        case WorkersUpdated(newWorkers) =>
            println("Это ЗИС: " + ctx.self)
            println("List: " + newWorkers)
                println(newWorkers.size)
                ctx.log.info("List of services registered with the receptionist changed: {}", newWorkers)
                running(ctx, newWorkers.toIndexedSeq, jobCounter)
        }
}
