package de.logger

import collection.JavaConversions._

object ExceptionHandler {

  def handle(exception: Throwable, replyJson: java.util.Map[String, Object]):Unit = {
    exception match {
      case failure: Failure => println(failure.getMessage)
      case _ => println(exception.getMessage);
    }
  }

}