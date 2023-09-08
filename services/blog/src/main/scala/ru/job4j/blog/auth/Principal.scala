package ru.checkdev.blog.auth
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * Case class represent authentication information.
  * @param username - user name
  * @param tokenValue - token value
  * @param authorities - sequence of authorities
  * @param key - unique user key
  * @version $Id$
  * @since 08.09.2018
  * */
case class Principal(
                      username: String,
                      tokenValue: String,
                      authorities: Seq[Authority],
                      key: String
                    )
/**
  * Companion Object Principal.
  * @version $Id$
  * @since 08.09.2018
  * */
object Principal {
  /** json Principal reads.*/
  implicit val jsonPrincipalReads: Reads[Principal] = (
    (JsPath \ "principal" \ "username").read[String] and
      (JsPath \ "details" \ "tokenValue").read[String] and
      (JsPath \ "principal" \ "authorities").read[Seq[Authority]] and
      (JsPath \ "principal" \ "key").read[String]) (Principal.apply _)
}

/**
  * Case class Authority represents authority.
  * @param authority - authority
  * @version $Id$
  * @since 08.09.2018
  * */
case class Authority(authority: String)
/**
  * Companion object Authority.
  * @version $Id$
  * @since 08.09.2018
  * */
object Authority {
  implicit val jsonAuthorityReads: Reads[Authority] = Json.reads[Authority]
}
