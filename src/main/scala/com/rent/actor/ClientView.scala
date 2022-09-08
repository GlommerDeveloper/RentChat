package viewChatController

import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.rent.actor.Worker
import com.rent.controller.ChatController
import javafx.application.Platform
import com.rent.model.Message
import com.rent.service.ChatService

import scala.concurrent.duration.DurationInt

object ClientView {
    sealed trait Event

    private case object Tick extends Event

    private final case class WorkersUpdated(newWorkers: Set[ActorRef[Worker.TransformText]]) extends Event

    private final case class TransformCompleted(originalText: String, transformedText: String) extends Event

    private final case class JobFailed(why: String, text: String) extends Event

    def apply(): Behavior[Event] = Behaviors.setup { ctx =>
        Behaviors.withTimers { timers =>
            // subscribe to available workers
            val subscriptionAdapter = ctx.messageAdapter[Receptionist.Listing] {
                case Worker.WorkerServiceKey.Listing(workers) =>
                    WorkersUpdated(workers)
            }
            ctx.system.receptionist ! Receptionist.Subscribe(Worker.WorkerServiceKey, subscriptionAdapter)

            timers.startTimerWithFixedDelay(Tick, Tick, 2.seconds)

            running(ctx, IndexedSeq.empty, jobCounter = 0)
        }
    }

    private def running(ctx: ActorContext[Event], workers: IndexedSeq[ActorRef[Worker.TransformText]], jobCounter: Int): Behavior[Event] =
        Behaviors.receiveMessage {
            case WorkersUpdated(newWorkers) =>
                ctx.log.info("List of services registered with the receptionist changed: {}", newWorkers)
                running(ctx, newWorkers.toIndexedSeq, jobCounter)
        }
}
