package ru.checkdev.blog.client

import scalaj.http.{Http, HttpRequest}

/**
  * Trait ClientHandler presents helpful methods simplified work with clients.
  * @version $Id$
  * @since 08.09.2018
  * */
trait ClientHandler {
  /**
    * Create http request.
    * @param url - uniform resource locator
    * @param method - request method type
    * @param body - body request
    * @param headers - request headers
    * @return http request
    * */
  def request(url: String, method: String, body: Option[String] = None, headers: Map[String, String] = Map.empty): HttpRequest = {
     val request = method match {
       case "GET" => Http(url)
       case "PUT" => Http(url).put(body.getOrElse(""))
       case "POST" => Http(url).postData(body.getOrElse(""))
     }
    request.copy(headers=headers.toSeq)
  }
  /**
    * Prepare request to protected resource,
    * according OAUTH 2.0 specification.
    * @param request - prepared request
    * @param token - access token
    * @return prepared request to protected resource
    * */
  def withAuth(request: HttpRequest, token: String): HttpRequest = request
    .headers(
      "User-Agent" -> "Mozilla/5.0",
      "Authorization" -> s"bearer ${token}",
      "Content-Type" -> "application/json",
      "charset" -> "utf-8"
    )

}
