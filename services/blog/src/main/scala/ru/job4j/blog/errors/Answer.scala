package ru.checkdev.blog.errors
import java.io.{PrintWriter, StringWriter}

import play.api.libs.json.{Reads, Writes, JsPath, Json, JsValue}
import play.api.libs.functional.syntax._

/**
  * Case class Answer represents content of response to request ending with error.
  * @param message - message
  * @param stackTrace - stack trace for error
  * @version $Id$
  * @since 08.09.2018
  * */
case class Answer(message: Option[String], stackTrace: Option[String] = None) {
  /**
    * Create from Answer json object.
    * @return json Answer
    * */
  def toJson: JsValue = Json.toJson(this)
}

/**
  * Companion object Answer.
  * @version $Id$
  * @since 08.09.2018
  * */
object Answer {
  /** json Answer reads.*/
  implicit val jsonMessageReads: Reads[Answer] = (
    (JsPath \"message").readNullable[String] and
      (JsPath \ "stackTrace").readNullable[String])(Answer.apply _)

  /** json Answer writes. */
  implicit val jsonMessageWrites: Writes[Answer] = new Writes[Answer] {
    override def writes(o: Answer): JsValue = {
      Json.obj(
        "message" -> o.message,
        "stackTrace" -> o.stackTrace
      )
    }
  }

  /**
    * Get exception stack trace as String.
    * @param e - exception
    * @return optional stack trace as String
    * */
  def stackTraceAsString(e: Throwable): Option[String] = {
    val sw: StringWriter = new StringWriter()
    val pw: PrintWriter = new PrintWriter(sw)
    e.printStackTrace(pw)
    Option(sw.toString)
  }
}

