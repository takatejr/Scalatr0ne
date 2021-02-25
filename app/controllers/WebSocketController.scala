package controllers

import play.api.mvc._
import play.api.libs.streams.ActorFlow
import javax.inject.Inject
import akka.actor.ActorSystem
import akka.stream.Materializer
import models.WebSocketActor
import play.api.libs.json._

class WebSocketsController @Inject() (cc: ControllerComponents)(implicit system: ActorSystem)
extends AbstractController(cc)
{
    val logger = play.api.Logger(getClass)

    def index = Action { implicit request: Request[AnyContent] =>
        logger.info("index page was called")
        Ok(views.html.index())
    }
 
    def ws = WebSocket.accept[JsValue, JsValue] { requestHeader =>
        logger.info("'ws' function is called")
        ActorFlow.actorRef { actorRef =>
            logger.info("ws: calling My Actor")
            WebSocketActor.props(actorRef)
        }
    }

}