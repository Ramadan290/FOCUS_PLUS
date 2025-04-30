package controllers

import play.api.mvc._
import services.SessionService
import java.util.UUID
import models.Session
import play.api.libs.json._
import org.mongodb.scala.model.{Filters, Updates}
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SessionController @Inject()(cc: ControllerComponents, sessionService: SessionService)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  /**
   * Starts a new session for a user.
   * If no userId exists in cookies, a new one is generated and stored in a cookie.
   */
  def startSession: Action[AnyContent] = Action.async { implicit request =>
    val userId = request.cookies.get("userId") match {
      case Some(cookie) => cookie.value
      case None =>
        val newUserId = UUID.randomUUID().toString
        Ok.withCookies(Cookie("userId", newUserId))
        newUserId
    }

    val newSession = Session(
      sessionId = UUID.randomUUID().toString,
      userId = userId,
      startTime = java.time.Instant.now.toString,
      endTime = Some(java.time.Instant.now.toString),
      duration = Some(0),
      createdAt = java.time.Instant.now.toString,
      laps = 0,
      streaks = 0
    )

    sessionService.createSession(newSession).map { message =>
      Ok(Json.obj("message" -> message, "userId" -> userId))
    }
  }

  /**
   * Stops an ongoing session.
   * Updates session details like duration, laps, and streaks.
   * Returns updated sessions for the user.
   */
  def stopSession: Action[AnyContent] = Action.async { implicit request =>
    request.body.asJson.flatMap(_.asOpt[Map[String, Any]]) match {
      case Some(data) =>
        val sessionId = data.get("sessionId").map(_.toString).getOrElse("")
        val endTime = java.time.Instant.now.toString
        val duration = data.get("duration").map(_.toString.toInt).getOrElse(0)
        val laps = data.get("laps").map(_.toString.toInt).getOrElse(0)
        val streaks = data.get("streaks").map(_.toString.toInt).getOrElse(0)
        val userId = data.get("userId").map(_.toString).getOrElse("")

        sessionService.stopSession(sessionId, duration, endTime, laps, streaks, userId).flatMap { message =>
          sessionService.getSessionsByUser(userId).map { sessions =>
            Ok(Json.obj("status" -> "success", "message" -> message, "sessions" -> Json.toJson(sessions)))
          }
        }.recover {
          case ex: Throwable =>
            InternalServerError(s"Error stopping session: ${ex.getMessage}")
        }

      case None =>
        Future.successful(BadRequest("Invalid data for stopping session"))
    }
  }

  /**
   * Adds a lap to an existing session.
   * @param sessionId The ID of the session to which the lap will be added.
   */
  def addLap(sessionId: String): Action[AnyContent] = Action.async {
    sessionService.addLap(sessionId).map { message =>
      Ok(Json.obj("status" -> "success", "message" -> message))
    }.recover {
      case ex: Throwable =>
        InternalServerError(s"Error adding lap: ${ex.getMessage}")
    }
  }

  /**
   * Fetches all sessions from the database.
   */
  def getAllSessions: Action[AnyContent] = Action.async { implicit request =>
    sessionService.getAllSessions().map { sessions =>
      Ok(views.html.sessions(sessions, "All Sessions")(request))
    }.recover {
      case e: Throwable =>
        InternalServerError(s"Error fetching all sessions: ${e.getMessage}")
    }
  }

  /**
   * Fetches all sessions for a specific user.
   * @param userId The ID of the user whose sessions will be fetched.
   */
def getSessionsForUser(userId: String): Action[AnyContent] = Action.async { implicit request =>
    sessionService.getSessionsByUser(userId).map { sessions =>
        // Format the startTime and endTime before sending to the frontend
        val formattedSessions = sessions.map { session =>
            session.copy(
                startTime = formatDateTime(session.startTime),
                endTime = session.endTime.map(formatDateTime) // Handle Option[String]
            )
        }
        Ok(Json.toJson(formattedSessions)) // Return formatted sessions
    }.recover {
        case e: Throwable =>
            InternalServerError(s"Error fetching sessions for user $userId: ${e.getMessage}")
    }
}

// Helper method to format ISO 8601 string to readable format
private def formatDateTime(isoString: String): String = {
    val instant = java.time.Instant.parse(isoString)
    val formatter = java.time.format.DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss")
      .withZone(java.time.ZoneId.systemDefault())
    formatter.format(instant)
}

  /**
   * Implicit JSON Reads for parsing JsValue to Map[String, Any].
   */
  implicit val mapReads: Reads[Map[String, Any]] = Reads { json =>
    json.validate[Map[String, JsValue]].map(_.map {
      case (key, value) => key -> parseJsonValue(value)
    })
  }

  /**
   * Parses a JsValue into a Scala Any type.
   * @param jsValue The JsValue to parse.
   */
  private def parseJsonValue(jsValue: JsValue): Any = jsValue match {
    case JsString(value) => value
    case JsNumber(value) => value.toBigIntExact.getOrElse(value)
    case JsBoolean(value) => value
    case JsArray(values) => values.map(parseJsonValue)
    case JsObject(fields) => fields.map { case (key, value) => key -> parseJsonValue(value) }
    case JsNull => null
  }

  /**
   * Retrieves the userId from cookies.
   * If no cookie is found, generates a new one.
   */
  private def getUserIdFromCookies(request: RequestHeader): String = {
    request.cookies.get("userId") match {
      case Some(cookie) => cookie.value
      case None =>
        UUID.randomUUID().toString
    }
  }
}
