package de.logger

import collection.JavaConversions._
import java.io.PrintWriter
import java.io.BufferedWriter
import java.io.FileWriter
import java.text.DateFormat
import java.util.Calendar

object ExceptionHandler {

  private val errorLog = new PrintWriter(new BufferedWriter(new FileWriter("error.log", true)), true)
  private val normalLog = new PrintWriter(new BufferedWriter(new FileWriter("log.log", true)), true)
  private val dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
  private val calendar = Calendar.getInstance;

  //Print start time and date of logging
  printLog("Logging started")
  printErrorLog("Logging started")

  def handle(throwable: Throwable, replyJson: java.util.Map[String, Object]): Unit = {
    throwable match {
      case failure: Failure => if (failure.getSendToUser) {
        replyJson.put("failure", failure.getMessage)
      }
      printLog(failure.getMessage)
      case error: Error => printErrorLog(error)
      case _ => printLog(throwable)
    }
  }

  def handle(message: String, caller: String): Unit = {
    handle(message, caller, null)
  }

  def handle(message: String, caller: String, cause: Throwable): Unit = {
    val temp = caller+" with Message: "+message
    cause match {
      case null => printLog(temp)
      case error: Error => printErrorLog(temp+" with cause:"); printErrorLog(error)
      case _ => printLog(temp+" with cause:"); printLog(cause)
    }
  }

  private def printLog(message : String):Unit = {
    normalLog.println(getTimeStamp()+" "+message)
  }
  
  private def printLog(throwable : Throwable):Unit = {
    normalLog.println(getTimeStamp()+" "+throwable.getMessage)
    throwable.printStackTrace(normalLog)
  }
  
  private def printErrorLog(error : Error):Unit = {
    errorLog.println(getTimeStamp()+" "+error.getMessage)
    error.printStackTrace(errorLog)
  }
  
  private def printErrorLog(message : String):Unit = {
    errorLog.println(getTimeStamp()+" "+message)
  }
  
  private def getTimeStamp():String ={
    dateFormat.format(calendar.getTime)
  }

}