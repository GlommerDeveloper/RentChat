//package com.rent.actor

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import com.rent.utils.CborSerializable
//
//object Worker {
//
//
//    trait Command
//
//
//    def apply(): Behavior[Command] =
//        Behaviors.setup { ctx =>
//            // each worker registers themselves with the receptionist
//            ctx.log.info("Registering myself with receptionist")
//            ctx.system.receptionist ! Receptionist.Register(WorkerServiceKey, ctx.self)
//
//            Behaviors.receiveMessage {
//                case TransformText(text, replyTo) =>
//                    replyTo ! TextTransformed(text.toUpperCase)
//                    Behaviors.same
//            }
//        }
//}
