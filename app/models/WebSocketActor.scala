package models

import akka.actor._
import play.api.libs.json._
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow

object WebSocketActor {
  def props(clientActorRef: ActorRef) = Props(new WebSocketActor(clientActorRef))
}

class WebSocketActor(clientActorRef: ActorRef) extends Actor {
  import WebSocketActor._
  import LogHandlerActor._

  val logger = play.api.Logger(getClass)

  logger.info(s"WebSocketActor class started")

  override def receive: Receive = {
    case msg: JsValue => {
      val clientMessage = getMessage(msg)
      val UserID = getUser(msg)

      val childRef = context.actorOf(LogHandlerActor.props(s"$UserID.txt", clientMessage), "LogHandler")

      childRef ! writeToLog(s"$UserID.txt", clientMessage)
    }
  }

  def getMessage(json: JsValue): String = { (json \ "message").as[String] }

  def getUser(json: JsValue): Int = { (json \ "UserID").as[Int] }
}
