package ru.job4j.blog.auth


import org.apache.oltu.oauth2.common.exception.OAuthProblemException
import org.apache.oltu.oauth2.common.message.types.ParameterStyle
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest
import org.scalatra.ScalatraBase
import play.api.libs.json.Json
import ru.job4j.blog.client.AuthorizationService
import ru.job4j.blog.errors.Answer

import scala.util.{Failure, Success, Try}
import ru.job4j.blog.errors.ServiceException.{ACCESS_TOKEN_ERROR, unauthorized}


/**
  * Trait OAuth help verify token and get authentication information,
  * according OAUTH 2.0 specification.
  * @version $Id$
  * @since 08.09.2018
  * */
trait OAuth extends  {
  sel: ScalatraBase =>

  /** authorization service. */
  private val authorizationService = AuthorizationService.LOAD

  /**
    * Verify token and get authentication information.
    * @return Principal
    * */
  def auth: Try[Principal] = try {
    val oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER)
    val token = oauthRequest.getAccessToken
    val response = authorizationService.user(token)
    if (response.code == 200) {
      Success(Json.parse(response.body).as[Principal])
    } else {
      Failure(unauthorized(Answer(Some(s"Not authorized. Authorization service return  Status: ${response.code}, Body: ${response.body}"))))
    }
  } catch {
    case e: OAuthProblemException => Failure(ACCESS_TOKEN_ERROR)
  }


}
