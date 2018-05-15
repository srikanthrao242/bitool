package org.com.bitool.server

import org.com.bitool.REQUEST

import scala.collection.Map
import scala.concurrent.Future
import scala.async.Async.{async, await}

/**
  * Created by srikanth on 4/28/18.
  */
import scala.concurrent.ExecutionContext
object Msg extends Enumeration {
  type Msg  = Value
  val CREATEDB,DEFAULT = Value
}
class Router[A] {
  import Msg._
  def run(ping:REQUEST[A])(implicit ec: ExecutionContext): Future[Boolean] =async{
    val mName = Msg.withName(ping.name)
    mName match{
      case CREATEDB=> await((new DataBase).createTable(ping.class_obj.asInstanceOf[Map[String,Any]]))
      //case DEFAULT=>
    }
  }
}
