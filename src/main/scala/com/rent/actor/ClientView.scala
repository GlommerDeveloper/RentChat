package com.rent.actor

import akka.actor.TypedActor.self
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import com.rent.model.Customer
import com.rent.service.ChatService
import com.rent.utils.CborSerializable
import javafx.application.Platform

import scala.language.postfixOps


object ClientView {

    val clientServiceKey: ServiceKey[Event] = ServiceKey[ClientView.Event]("Client")
    var clientInCluster: Customer = _


    sealed trait Event

    private final case class ClientsUpdated(newClients: Set[ActorRef[ClientView.Event]]) extends Event with CborSerializable

    case class NewClient(clientPort: Int, clientNickName: String) extends Event with CborSerializable

    case class InfoToNewClient(client: Customer) extends Event with CborSerializable

    case class MyInfo(client: Customer, answerTo: ActorRef[Event]) extends Event with CborSerializable


    def apply(controller: ChatService, nickname: String, port: Int): Behavior[Event] = Behaviors.setup { ctx =>

        ctx.system.receptionist ! Receptionist.Register(clientServiceKey, ctx.self)

        val subscriptionAdapter = ctx.messageAdapter[Receptionist.Listing] {
            case ClientView.clientServiceKey.Listing(clients) =>
                ClientsUpdated(clients)
                NewClient(port, nickname)
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
                clientInCluster = new Customer(clientPort, clientNickName, ctx.self)
                Platform.runLater(() =>controller.newUser(clientInCluster))
                clients.foreach(actor => if(actor != ctx.self) actor ! MyInfo(clientInCluster, ctx.self))
                Behaviors.same

            case MyInfo(receivedClient, answerTo) =>
                println("------OTHER ACTOR TAKE NEW ACTOR------")
                Platform.runLater(() => controller.newUser(receivedClient))
                answerTo ! InfoToNewClient(clientInCluster)
                Behaviors.same

            case InfoToNewClient(client) =>
                println("------IN SEND_NEW_CLIENT CASE------")
                Platform.runLater(() => controller.newUser(client))
                Behaviors.same
        }

}
