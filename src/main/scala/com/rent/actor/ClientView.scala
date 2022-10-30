package com.rent.actor

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import com.rent.RentApp.clientActor
import com.rent.model.{Customer, Message}
import com.rent.service.ChatService
import javafx.application.Platform

import scala.language.postfixOps


object ClientView {

    val clientServiceKey: ServiceKey[Event] = ServiceKey[ClientView.Event]("Client")
    var clientInCluster: Customer = _
    var currentCountClients: IndexedSeq[ActorRef[ClientView.Event]] = _

    trait JSer

    sealed trait Event extends JSer

    private final case class ClientsUpdated(newClients: Set[ActorRef[ClientView.Event]]) extends Event

    case class NewClient(clientPort: Int, clientNickName: String) extends Event

    case class MyInfo(client: Customer, answerTo: ActorRef[Event]) extends Event

    case class PostMessage(message: Message, friend: Customer) extends Event

    case class PostMessageToGeneral(message: Message, fromFriend: Customer, generalRoom: Customer) extends Event

    case class StopActor() extends Event

    case class DeleteStoppedActor(friend: Customer) extends Event


    def apply(controller: ChatService): Behavior[Event] = Behaviors.setup { ctx =>
        init(ctx, controller)
    }

    private def init(ctx: ActorContext[Event], controller: ChatService): Behavior[Event] = {
        Behaviors.receiveMessage {
            case NewClient(clientPort, clientNickName) =>
                println("------IN NEW_CLIENT CASE------" + "\nport: " + clientPort + "\nnick: " + clientNickName)
                clientActor = ctx.self
                clientInCluster = new Customer(clientPort, clientNickName, ctx.self)
                Platform.runLater(() => controller.setMySelf(clientInCluster))

                ctx.system.receptionist ! Receptionist.Register(clientServiceKey, ctx.self)

                val subscriptionAdapter = ctx.messageAdapter[Receptionist.Listing] {
                    case ClientView.clientServiceKey.Listing(clients) =>
                        ClientsUpdated(clients)
                }

                ctx.system.receptionist ! Receptionist.Subscribe(ClientView.clientServiceKey, subscriptionAdapter)

                running(ctx, IndexedSeq(), controller, clientPort, clientNickName)
        }
    }

    private def running(ctx: ActorContext[Event], clients: IndexedSeq[ActorRef[ClientView.Event]], controller: ChatService,
                        clientPort: Int, clientNickName: String): Behavior[Event] =
        Behaviors.receiveMessage {
            case ClientsUpdated(newClients) =>
                currentCountClients = newClients.toIndexedSeq
                println("Size newClients: " + newClients.size)
                ctx.log.info("///// List of services registered with the receptionist changed: {}", newClients)
                newClients.foreach(actor => if (actor != ctx.self) actor ! MyInfo(new Customer(clientPort, clientNickName, ctx.self), ctx.self))
                running(ctx, currentCountClients, controller, clientPort, clientNickName)

            case MyInfo(receivedClient, answerTo) =>
                println("------OTHER ACTOR TAKE NEW ACTOR------")
                Platform.runLater(() => controller.newUser(receivedClient))
                Behaviors.same

            case PostMessage(message, friend) =>
                Platform.runLater(() => controller.setMessageFromOtherActor(message, friend))
                Behaviors.same

            case PostMessageToGeneral(message, fromFriend, generalRoom) =>
                currentCountClients.foreach(actor => if (actor != ctx.self) actor ! PostMessage(message, generalRoom))
                Behaviors.same

            case StopActor() =>
                println("---CASE STOPaCTOR---")
                currentCountClients.foreach(actor => if (actor != ctx.self) actor ! DeleteStoppedActor(clientInCluster))
                Behaviors.stopped

            case DeleteStoppedActor(friend) =>
                Platform.runLater(() => controller.deleteUser(friend))
                Behaviors.same
        }
}
