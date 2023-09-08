package ru.checkdev.blog.config

import com.typesafe.config.ConfigFactory

/**
  * Class AppConfig represents access to application configurations.
  * @version $Id$
  * @since 08.09.2018
  * */
class AppConfig private {
  /** configuration to application. */
  private val conf  = ConfigFactory.load()
  /**
    * Get access to authorization service endpoint.
    * @return authorization service endpoint
    * */
  def endpointAuthorizationService: String  = conf.getString("endpoints.authorizationService")
}

/**
  * Companion object AppConfig.
  * @version $Id$
  * @since 08.09.2018
  * */
object AppConfig {
  /**application configuration instance.*/
  val LOAD = new AppConfig
}

