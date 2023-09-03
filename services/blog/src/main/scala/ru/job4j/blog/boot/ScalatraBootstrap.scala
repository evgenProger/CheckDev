package ru.job4j.blog.boot

import javax.servlet.ServletContext
import org.scalatra.LifeCycle
import ru.job4j.blog.controllers.BlogController

/**
  * Class ScalatraBootstrap represents special Scalatra framework class.
  * It helps load controllers.
  * @version $Id$
  * @since 08.09.2018
  * */
class ScalatraBootstrap extends LifeCycle {
  /**
    * Initialize controllers.
    * @param context - servlet context
    * */
  override def init(context: ServletContext): Unit = {
    context.mount(new BlogController, "/")
  }
}
