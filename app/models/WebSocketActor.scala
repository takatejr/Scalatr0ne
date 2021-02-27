package models

import akka.actor._
import play.api.libs.json._
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow

object WebSocketActor {
    def props(clientActorRef: ActorRef) = Props(new WebSocketActor(clientActorRef))
}

class WebSocketActor(clientActorRef: ActorRef) extends Actor {
    import LogHandlerActor._
    val logger = play.api.Logger(getClass)
    
    logger.info(s"WebSocketActor class started")

    def receive = {
        case jsValue: JsValue =>
            println(s"$jsValue")
            logger.info(s"JS-VALUE: $jsValue")
            val clientMessage = getMessage(jsValue)
            val json: JsValue = Json.parse(s"""{"body": "You said, ‘$clientMessage’"}""")
            val childRef = context.actorOf(Props[LogHandlerActor], "LogHandler")
            context.become(handleLogger(childRef))
            clientActorRef ! (json)
    }

    def getMessage(json: JsValue): String = (json \ "message").as[String]

    
    def handleLogger(childRef: ActorRef): Receive = {
      case writeToLog(message) => childRef forward message
    }

}