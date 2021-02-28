package models

import akka.actor._
import play.api.libs.json._
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow

object WebSocketActor {

  case class LogHandler(ClientMsg: JsValue)
  def props(clientActorRef: ActorRef) = Props(
    new WebSocketActor(clientActorRef)
  )
  val isActorCreated = false
}

class WebSocketActor(clientActorRef: ActorRef) extends Actor {
  import WebSocketActor._
  import LogHandlerActor._

  val logger = play.api.Logger(getClass)

  logger.info(s"WebSocketActor class started")

  override def receive = handleLogger(isActorCreated)

  def handleLogger(isActorCreated: Boolean): Receive = {
    case LogHandler(jsVal) => {
      val clientMessage = getMessage(jsVal)
      val UserID = getUser(jsVal)

      println(s"$jsVal")
      logger.info(s"JS-VALUE: $jsVal")

      val json: JsValue =
        Json.parse(s"""{"body": "You said, ‘$clientMessage’"}""")

      if (isActorCreated < true) {
        val childRef = context.actorOf(
          LogHandlerActor.props(s"$UserID.txt", clientMessage),
          "LogHandler"
        )

        context.become(handleSendToLogger(childRef))
      }

      // clientActorRef ! (json)
        clientActorRef ! writeToLog(s"$UserID.txt", clientMessage)
    }
  }

  def getMessage(json: JsValue): String = { (json \ "message").as[String] }

  def getUser(json: JsValue): Int = { (json \ "UserID").as[Int] }


  def handleSendToLogger(childRef: ActorRef): Receive = {
    case writeToLog(filePath, message) => childRef forward writeToLog(filePath, message)
  }

}
