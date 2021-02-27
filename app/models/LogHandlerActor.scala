package models

import akka.actor._
import java.io.BufferedReader
import java.io._
import scala.io.Source
import java.io.BufferedWriter
import java.io.FileOutputStream
import play.api.libs.json.JsValue


object LogHandlerActor {
  case class writeToLog(message: String)
}

class LogHandlerActor extends Actor {
  import LogHandlerActor._
  def receive: Receive = logger

  def logger: Receive = {
    case writeToLog(message) => {
      val log = new PrintWriter(new FileOutputStream(new File(filePath)))

      log.write(message)
      log.close()
    }
  }
}
