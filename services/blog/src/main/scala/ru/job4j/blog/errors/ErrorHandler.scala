package ru.checkdev.blog.errors

import org.scalatra.{ActionResult, BadRequest, InternalServerError, Unauthorized}
import play.api.libs.json.Json

/**
  * Object ErrorHandler represents service exception handler.
  * Prepare answer for error http request.
  * @version $Id$
  * @since 08.09.2018
  * */
object ErrorHandler {
  /** content-type header. */
  private val CONTENT_TYPE_HEADER = "Content-Type" -> "application/json"

  /**handler. */
  val HANDLER: PartialFunction[Throwable, ActionResult] = {
    case ServiceException(400, answer) => BadRequest(answer.toJson, Map(CONTENT_TYPE_HEADER))
    case ServiceException(401, answer) => Unauthorized(answer.toJson, Map(CONTENT_TYPE_HEADER))
    case undefined: Throwable =>
      val answer = Answer(Some("Cannot determine error type"), Answer.stackTraceAsString(undefined))
      InternalServerError(Json.toJson(answer), Map(CONTENT_TYPE_HEADER))
  }
}
