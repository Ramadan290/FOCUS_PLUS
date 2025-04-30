package models

import org.mongodb.scala.bson.collection.immutable.Document
import play.api.libs.json._
import java.time.{Instant, ZoneId}
import java.time.format.DateTimeFormatter
import org.bson.BsonDateTime

/**
 * Case class representing a session.
 *
 * @param sessionId Unique ID for the session.
 * @param userId User ID associated with the session.
 * @param startTime Start time of the session (stored as String).
 * @param endTime Optional end time of the session (stored as Option[String]).
 * @param duration Optional duration of the session (in seconds).
 * @param createdAt Creation timestamp for the session.
 * @param laps Number of laps recorded in the session.
 * @param streaks Number of streaks achieved in the session.
 */
case class Session(
  sessionId: String,
  userId: String,
  startTime: String,
  endTime: Option[String],
  duration: Option[Int],
  createdAt: String,
  laps: Int,
  streaks: Int
)

object Session {

  /**
   * Converts a `Session` case class to a MongoDB Document for storage.
   *
   * @param session The session to convert.
   * @return A MongoDB `Document` representation of the session.
   */
  def toDocument(session: Session): Document = {
    Document(
      "sessionId" -> session.sessionId,
      "userId" -> session.userId,
      "startTime" -> new BsonDateTime(Instant.parse(session.startTime).toEpochMilli),
      "endTime" -> session.endTime.map(e => new BsonDateTime(Instant.parse(e).toEpochMilli)),
      "duration" -> session.duration,
      "createdAt" -> session.createdAt,
      "laps" -> session.laps,
      "streaks" -> session.streaks
    )
  }

  /**
   * Converts a MongoDB Document to a `Session` case class.
   *
   * @param doc The MongoDB Document to convert.
   * @return A `Session` instance.
   */
  def fromDocument(doc: Document): Session = {
    val startTime = doc.get("startTime").map(e => formatDateTime(Instant.ofEpochMilli(e.asDateTime().getValue)))
    val endTime = doc.get("endTime").map(e => formatDateTime(Instant.ofEpochMilli(e.asDateTime().getValue)))

    Session(
      sessionId = doc.getString("sessionId"),
      userId = doc.getString("userId"),
      startTime = startTime.getOrElse(formatDateTime(Instant.now())),
      endTime = endTime,
      duration = doc.get("duration").map(_.asInt32().getValue),
      createdAt = formatDateTime(Instant.parse(doc.getString("createdAt"))),
      laps = doc.get("laps").map(_.asInt32().getValue).getOrElse(0),
      streaks = doc.get("streaks").map(_.asInt32().getValue).getOrElse(0)
    )
  }

  /**
   * Formats an `Instant` into a user-friendly string.
   *
   * @param instant The `Instant` to format.
   * @return A formatted string in the "yyyy-MM-dd HH:mm:ss" format.
   */
  private def formatDateTime(instant: Instant): String = {
    val formatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss")
      .withZone(ZoneId.systemDefault())
    formatter.format(instant)
  }

  /**
   * Implicit JSON format for the `Session` case class.
   * Enables easy conversion between `Session` objects and JSON.
   */
  implicit val sessionFormat: OFormat[Session] = Json.format[Session]
}
