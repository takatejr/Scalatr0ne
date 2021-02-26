package models

import akka.actor._
import java.io.BufferedReader
import java.io._
import scala.io.Source
import java.io.BufferedWriter
import java.io.FileOutputStream
import play.api.libs.json.JsValue


object LogHandlerActor {
  def props(clientActorRef: ActorRef) = Props(
    new WebSocketActor(clientActorRef)
  )
}

class LogHandlerActor(clientActorRef: ActorRef) extends Actor {

  def receive: Receive = {
    case jsValue: JsValue => {
        val (filePath, content) = jsValue
      val log = new PrintWriter(new FileOutputStream(new File(jsValue.filePath), true)))

      log.write(jsValue.content)
      log.close()
    }
  }

}
