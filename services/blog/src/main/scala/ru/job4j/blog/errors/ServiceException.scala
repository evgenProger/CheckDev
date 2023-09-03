package ru.job4j.blog.errors

/**
  * Case class ServiceException help wrapped exception occuring in service application.
  * @param status - http response status for occuring exception
  * @param answer - answer for occuring exception
  * @version $Id$
  * @since 08.09.2018
  * */
case class ServiceException (status: Int, answer: Answer) extends Exception

/**
  * Companion object ServiceException.
  * @version $Id$
  * @since 08.09.2018
  * */
object  ServiceException {
  /** access token exception.*/
  val ACCESS_TOKEN_ERROR = ServiceException(400, Answer(Some("Cannot get access token from request")))
  /**
    * Exception on authorized user.
    * @param answer - answer
    * @return ServiceException when user not authorized.
    * */
  def unauthorized(answer: Answer) = ServiceException(401, answer)

}


