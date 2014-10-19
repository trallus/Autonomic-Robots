package de.logger

import collection.JavaConversions._

class ExceptionHandler(exception : Throwable, replyJson : java.util.Map[String,Object]) {
  
  exception match{
    case failure : Failure => println(failure.getMessage)
    case _ => println(exception.getMessage);
  }
  
}