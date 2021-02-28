package models

import akka.actor._
import java.io.BufferedReader
import java.io._
import scala.io.Source
import java.io.BufferedWriter
import java.io.FileOutputStream
import play.api.libs.json.JsValue


object LogHandlerActor {
  def props(filePath: String, message: String) = Props(new LogHandlerActor(filePath: String, message: String))
  case class writeToLog(filePath: String, message: String)
}

class LogHandlerActor(filePath: String, message: String) extends Actor {
  import LogHandlerActor._

  override def receive: Receive = logger

  def logger: Receive = {
    case writeToLog(filePath, message) => {
      val log = new PrintWriter(new FileOutputStream(new File(filePath)))
      println(message)
      log.write(message)
      log.close()
    }
  }
}
