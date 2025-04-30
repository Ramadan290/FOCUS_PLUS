package services

import models.Session
import javax.inject.Inject
import org.mongodb.scala.model.{Filters, Updates, Sorts}
import org.mongodb.scala.bson.collection.immutable.Document
import scala.concurrent.{ExecutionContext, Future}
import utils.MongoDbUtil
import java.time.Instant
import org.mongodb.scala._
import org.mongodb.scala.bson.collection.immutable.Document

/**
 * Service class for managing session operations.
 *
 * @param ec The execution context for handling asynchronous operations.
 */
class SessionService @Inject()(implicit ec: ExecutionContext) {

  // MongoDB collection for sessions
  private val collection: MongoCollection[Document] = MongoDbUtil.getCollection("sessions")

  /**
   * Creates a new session.
   * If a session with the same sessionId already exists, it skips creation.
   *
   * @param session The session to create.
   * @return A message indicating the result of the operation.
   */
  def createSession(session: Session): Future[String] = {
    collection.find(Filters.eq("sessionId", session.sessionId)).first().toFuture().flatMap { existingSession =>
      if (existingSession == null) {
        collection.insertOne(Session.toDocument(session)).toFuture().map { _ =>
          "Session started successfully"
        }
      } else {
        Future.successful("Session already exists")
      }
    }.recover {
      case ex: Throwable =>
        println(s"Error creating session: ${ex.getMessage}")
        "Failed to start session"
    }
  }

  /**
   * Stops a session by updating its details.
   * If the session does not exist, a new session is created with the provided details.
   *
   * @param sessionId The ID of the session to stop.
   * @param duration The duration of the session in seconds.
   * @param endTime The end time of the session as a String.
   * @param laps The number of laps recorded in the session.
   * @param streaks The number of streaks achieved in the session.
   * @param userId The user ID associated with the session.
   * @return A message indicating the result of the operation.
   */
  def stopSession(sessionId: String, duration: Int, endTime: String, laps: Int, streaks: Int, userId: String): Future[String] = {
    if (sessionId.isEmpty) {
      Future.successful("Invalid sessionId provided")
    } else {
      val update = Updates.combine(
        Updates.set("endTime", endTime),
        Updates.set("duration", duration),
        Updates.set("laps", laps),
        Updates.set("streaks", streaks),
        Updates.set("userId", userId)
      )

      collection.find(Filters.eq("sessionId", sessionId)).first().toFuture().flatMap { document =>
        if (document == null) {
          val newSession = Session(
            sessionId = sessionId,
            userId = userId,
            startTime = Instant.now().toString,
            endTime = Some(endTime),
            duration = Some(duration),
            createdAt = Instant.now().toString,
            laps = laps,
            streaks = streaks
          )
          collection.insertOne(Session.toDocument(newSession)).toFuture().map { _ =>
            "New session created successfully"
          }
        } else {
          collection.updateOne(Filters.eq("sessionId", sessionId), update).toFuture().map { result =>
            if (result.getModifiedCount > 0) "Session stopped successfully"
            else "Session update failed"
          }
        }
      }.recover {
        case ex: Throwable =>
          println(s"Error updating session with sessionId $sessionId: ${ex.getMessage}")
          "An error occurred while stopping the session"
      }
    }
  }

  /**
   * Increments the lap count for a session.
   *
   * @param sessionId The ID of the session to update.
   * @return A message indicating the result of the operation.
   */
  def addLap(sessionId: String): Future[String] = {
    collection.updateOne(Filters.eq("sessionId", sessionId), Updates.inc("laps", 1)).toFuture().map { result =>
      if (result.getModifiedCount > 0) "Lap added successfully"
      else "Session not found or lap update failed"
    }.recover {
      case ex: Throwable =>
        println(s"Error adding lap for sessionId $sessionId: ${ex.getMessage}")
        "An error occurred while adding a lap"
    }
  }

  /**
   * Fetches all sessions from the database.
   *
   * @return A sequence of all session objects.
   */
  def getAllSessions(): Future[Seq[Session]] = {
    collection.find().toFuture().map { documents =>
      documents.map(Session.fromDocument)
    }.recover {
      case ex: Throwable =>
        println(s"Error fetching all sessions: ${ex.getMessage}")
        Seq.empty
    }
  }

  /**
   * Fetches sessions for a specific user, sorted by startTime (newest first).
   *
   * @param userId The user ID whose sessions are to be fetched.
   * @return A sequence of session objects for the user.
   */
  def getSessionsByUser(userId: String): Future[Seq[Session]] = {
    collection.find(Filters.eq("userId", userId))
      .sort(Sorts.descending("startTime"))
      .toFuture().map { documents =>
        documents.map(Session.fromDocument)
      }.recover {
        case ex: Throwable =>
          println(s"Error fetching sessions for userId $userId: ${ex.getMessage}")
          Seq.empty
      }
  }

  /**
   * Fetches a specific session by its ID.
   *
   * @param sessionId The ID of the session to fetch.
   * @return An optional session object if found.
   */
  def getSessionById(sessionId: String): Future[Option[Session]] = {
    collection.find(Filters.eq("sessionId", sessionId)).first().toFuture().map { document =>
      if (document == null) None
      else Some(Session.fromDocument(document))
    }.recover {
      case ex: Throwable =>
        println(s"Error fetching session with sessionId $sessionId: ${ex.getMessage}")
        None
    }
  }
}
