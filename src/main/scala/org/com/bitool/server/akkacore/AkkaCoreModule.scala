package org.com.bitool.server.akkacore

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

/**
  * MacWire module for mixing in to the main app
  *
  * @author srikanth
  */
trait AkkaCoreModule {

  implicit val actorSystem: ActorSystem = ActorSystem()

  implicit val actorSystemDispatcher: ExecutionContext = actorSystem.dispatcher

  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()

}
