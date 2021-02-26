package models

import akka.actor._
import play.api.libs.json._
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow

object WebSocketActor {
    def props(clientActorRef: ActorRef) = Props(new WebSocketActor(clientActorRef))
}

class WebSocketActor(clientActorRef: ActorRef) extends Actor {
    val logger = play.api.Logger(getClass)

    logger.info(s"WebSocketActor class started")

    def receive = {
        case jsValue: JsValue =>
            println(s"$jsValue")
            logger.info(s"JS-VALUE: $jsValue")
            val clientMessage = getMessage(jsValue)
            val json: JsValue = Json.parse(s"""{"body": "You said, ‘$clientMessage’"}""")
            clientActorRef ! (json)
    }

    def getMessage(json: JsValue): String = (json \ "message").as[String]

}