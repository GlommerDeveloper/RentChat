package actor

import actor.ChatFrameActor.Event
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import controller.ChatController


object ChatFrameActor{
  trait Event
  final case class Die () extends Event
  final case class SendMessageF(message: String) extends Event

  def apply(controller: ChatController): Behavior[Event] =
    Behaviors.setup(context =>
      new ChatFrameActor(context, controller)
    )

  def main(): Behavior[Event] =
    Behaviors.setup { context =>
      Behaviors.receiveMessage {
        case Die() =>
          println("hello")
          Behaviors.same

        case SendMessageF(str) =>
          println("IDontKnow")
          Behaviors.same
      }
    }
}

class ChatFrameActor(context: ActorContext[Event], chatWindow: ChatController) extends AbstractBehavior[Event](context) {

  override def onMessage(msg: Event): Behavior[Event] = {
//    val currentLogin = chatWindow.login
//    msg match {
//
//    }
    ???
  }
}
