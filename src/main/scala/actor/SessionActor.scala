package actor

import actor.ChatFrameActor.Event
import akka.actor.typed.Behavior
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.scaladsl.Behaviors
import controller.ChatController

class SessionActor() {

    def apply(scalaChatWindow: ChatController): Behavior[Event] = start(scalaChatWindow)

    private def start(scalaChatWindow: ChatController):Behavior[Event] = Behaviors.setup({ context =>
        val actor = context.spawn(ChatFrameActor(scalaChatWindow), "user")
        val topic = context.spawn(Topic[Event]("topic"), "topic")

        topic ! Topic.Subscribe(actor)

        Behaviors.receiveMessage {
            msg: Event =>
                topic ! Topic.Publish(msg)
                Behaviors.same
        }
    })
}
