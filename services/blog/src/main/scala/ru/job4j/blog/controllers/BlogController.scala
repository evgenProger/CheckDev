package ru.checkdev.blog.controllers
import org.scalatra.ScalatraServlet
import ru.checkdev.blog.auth.OAuth
import ru.checkdev.blog.errors.ErrorHandler

import scala.util.{Failure, Success}

/**
  * Class BlogController represents controller for "blog" service.
  * @version $Id$
  * @since 08.09.2018
  * */
class BlogController extends ScalatraServlet with OAuth {

  before() {
    auth match {
      case Failure(ex) => throw ex
      case Success(principal) => request.setAttribute("principal", principal)
    }
  }
  get("/HelloWorld") {
    request.getAttribute("principal").toString
  }
  error(ErrorHandler.HANDLER)
}
