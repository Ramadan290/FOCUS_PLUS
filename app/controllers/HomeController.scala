package controllers

import play.api.mvc._
import services.SessionService
import models.Session
import play.api.libs.json._
import utils.MongoDbUtil
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import java.time.{Instant, ZoneId}
import java.time.format.DateTimeFormatter
import java.util.UUID

@Singleton
class HomeController @Inject()(
  cc: ControllerComponents,
  sessionService: SessionService
)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  // Renders the home page and displays all sessions
  def index: Action[AnyContent] = Action.async { implicit request =>
    sessionService.getAllSessions().map { sessions =>
      Ok(views.html.index(sessions, "Welcome to Focus+"))
    }
  }

  private def generateRandomString(): String = UUID.randomUUID().toString

  private def getUserIdFromCookies(request: RequestHeader): String = {
    request.cookies.get("userId") match {
      case Some(cookie) => cookie.value
      case None => generateRandomString()
    }
  }

  def startSession: Action[AnyContent] = Action.async { implicit request =>
    val userId = request.cookies.get("userId") match {
      case Some(cookie) => cookie.value
      case None =>
        val newUserId = generateRandomString()
        Ok.withCookies(Cookie("userId", newUserId))
        newUserId
    }

    val newSession = Session(
      sessionId = UUID.randomUUID().toString,
      userId = userId,
      startTime = java.time.Instant.now.toString,
      endTime = None,
      duration = None,
      createdAt = java.time.Instant.now.toString,
      laps = 0,
      streaks = 0
    )

    sessionService.createSession(newSession).map { _ =>
      Ok(Json.obj("message" -> "Session started successfully", "userId" -> newSession.userId))
    }
  }

  def getUserSessions(userId: String): Action[AnyContent] = Action.async { implicit request =>
    sessionService.getSessionsByUser(userId).map { sessions =>
      Ok(Json.toJson(sessions))
    }.recover {
      case e: Throwable =>
        InternalServerError(s"Error fetching sessions for user $userId: ${e.getMessage}")
    }
  }

  // Stops an ongoing session
  def stopSession: Action[AnyContent] = Action.async { implicit request =>
    request.body.asJson.flatMap(_.asOpt[Map[String, Any]]) match {
      case Some(data) =>
        try {
          val sessionId = data.getOrElse("sessionId", "").toString
          val duration = data.getOrElse("duration", "0").toString.toInt
          val laps = data.getOrElse("laps", "0").toString.toInt
          val streaks = data.getOrElse("streaks", "0").toString.toInt
          val userId = data.getOrElse("userId", "").toString
          val endTime = java.time.Instant.now.toString

          if (sessionId.isEmpty || userId.isEmpty) {
            Future.successful(BadRequest(Json.obj(
              "status" -> "error",
              "message" -> "Missing required fields: sessionId or userId"
            )))
          } else {
            sessionService.stopSession(sessionId, duration, endTime, laps, streaks, userId).map { message =>
              Ok(Json.obj(
                "status" -> "success",
                "message" -> message,
                "endTime" -> formatDateTime(Instant.parse(endTime))
              ))
            }.recover {
              case ex: Throwable =>
                InternalServerError(Json.obj(
                  "status" -> "error",
                  "message" -> s"Error stopping session: ${ex.getMessage}"
                ))
            }
          }
        } catch {
          case ex: Exception =>
            Future.successful(BadRequest(Json.obj(
              "status" -> "error",
              "message" -> s"Invalid data: ${ex.getMessage}"
            )))
        }

      case None =>
        Future.successful(BadRequest(Json.obj(
          "status" -> "error",
          "message" -> "Invalid or missing data in request body"
        )))
    }
  }

  def addLap(sessionId: String): Action[AnyContent] = Action.async {
    sessionService.addLap(sessionId).map { message =>
      Ok(Json.obj("status" -> "success", "message" -> message))
    }
  }

  implicit val mapReads: Reads[Map[String, Any]] = Reads { json =>
    json.validate[Map[String, JsValue]].map(_.map {
      case (key, value) => key -> parseJsonValue(value)
    })
  }

  private def parseJsonValue(jsValue: JsValue): Any = jsValue match {
    case JsString(value) => value
    case JsNumber(value) => value.toBigIntExact.getOrElse(value)
    case JsBoolean(value) => value
    case JsArray(values) => values.map(parseJsonValue)
    case JsObject(fields) => fields.map { case (key, value) => key -> parseJsonValue(value) }
    case JsNull => null
  }

  private def formatDateTime(instant: Instant): String = {
    val formatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss")
      .withZone(ZoneId.systemDefault())
    formatter.format(instant)
  }

  def testDbConnection: Action[AnyContent] = Action.async {
    val collection = MongoDbUtil.getCollection("sessions")
    collection.find().toFuture().map { documents =>
      Ok(s"Successfully connected! Found ${documents.size} documents.")
    }.recover {
      case e: Throwable => InternalServerError(s"Failed to connect to MongoDB: ${e.getMessage}")
    }
  }
}
