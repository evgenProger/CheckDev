package ru.job4j.blog.client

import ru.job4j.blog.config.AppConfig
import scalaj.http.HttpResponse

/**
  * Class AuthorizationService represents client for Authorization Service.
  * @param appConfig -"blog"  service configuration parameters
  * @version $Id$
  * @since 08.09.2018
  * */
class AuthorizationService private(val appConfig: AppConfig) extends ClientHandler {
  /**
    * Request for verify token and get authentication information.
    * @param token - verified token
    * @return response with authentication information
    * */
  def user(token: String): HttpResponse[String] = {
    withAuth(request(s"${appConfig.endpointAuthorizationService}/user", "GET"), token).asString
  }
}

/**
  * Companion object AuthorizationService.
  * @version $Id$
  * @since 08.09.2018
  * */
object AuthorizationService {
  /** authorization service instance.*/
  val LOAD = new AuthorizationService(AppConfig.LOAD)
}
