package com.rent.actor

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import akka.util.Timeout
import com.rent.model.{Client, Message}
import com.rent.service.ChatService
import com.rent.utils.CborSerializable
import javafx.application.Platform

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps


object ClientView {

    val clientServiceKey: ServiceKey[Event] = ServiceKey[ClientView.Event]("Client")

    var clientInCluster: Client = _

    sealed trait Event

    private final case class ClientsUpdated(newClients: Set[ActorRef[ClientView.Event]]) extends Event with CborSerializable

    case class NewClient(clientPort: Int, clientNickName: String) extends Event with CborSerializable

    case class PostMessage(message: Message) extends Event with CborSerializable

    case class SendNewClient(actorRef: ActorRef[Event]) extends Event with CborSerializable

    case class AskName(answerTo: ActorRef[Event]) extends Event with CborSerializable

    case class MyInfo(client: Client) extends Event with CborSerializable


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
                ctx.log.info("///// List of services registered with the receptionist changed: {}", newClients)
                running(ctx, newClients.toIndexedSeq, controller)

            case NewClient(clientPort, clientNickName) =>
                println("------IN NEW_CLIENT CASE------" + "\nport: " + clientPort + "\nnick: " + clientNickName )
                clientInCluster = new Client(clientPort, clientNickName, ctx.self)
                clients.foreach(actor => {
                    actor ! SendNewClient(ctx.self)
                    if(actor != ctx.self) actor ! MyInfo(clientInCluster)
                })
                Behaviors.same

            case SendNewClient(actorRef) =>
                println("------IN SEND_NEW_CLIENT CASE------")
                actorRef ! MyInfo(clientInCluster)
                Behaviors.same

            case MyInfo(receivedClient) =>
                Platform.runLater(() => controller.newUser(receivedClient))
                Behaviors.same
        }
}
