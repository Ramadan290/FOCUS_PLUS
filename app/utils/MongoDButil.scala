package utils

import org.mongodb.scala._
import com.typesafe.config.ConfigFactory
import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId}

/**
 * Utility object for managing MongoDB connections and operations.
 */
object MongoDbUtil {

  // Load configuration settings from application.conf
  private val config = ConfigFactory.load()
  private val uri: String = config.getString("mongodb.uri")
  private val databaseName: String = config.getString("mongodb.database")

  // Initialize the MongoDB client
  private val client: MongoClient = MongoClient(uri)
  private val database: MongoDatabase = client.getDatabase(databaseName)

  /**
   * Fetches a MongoDB collection by name.
   *
   * @param collectionName The name of the collection to retrieve.
   * @return The MongoDB collection as a `MongoCollection[Document]`.
   */
  def getCollection(collectionName: String): MongoCollection[Document] = {
    database.getCollection(collectionName)
  }

  /**
   * Formats an ISO date string to "YYYY-MM-DD HH:mm:ss".
   * Removes "T" and "Z" from the ISO format.
   *
   * @param isoDate The ISO date string to format.
   * @return A formatted date string in "YYYY-MM-DD HH:mm:ss".
   */
  def formatDate(isoDate: String): String = {
    try {
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"))
      val instant = Instant.parse(isoDate)
      formatter.format(instant)
    } catch {
      case _: Throwable => "Invalid Date"
    }
  }
}
