package org.com.bitool.server

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.{ExecutorService, Executors, ThreadFactory}

import org.com.bitool.REQUEST

import scala.async.Async.{async, await}
import scala.collection.immutable.HashMap
import scala.concurrent.ExecutionContext
/**
  * Created by srikanth on 5/1/18.
  */
class Main {

  private val ioThreadPool: ExecutorService = Executors.newCachedThreadPool(
    new ThreadFactory {
      private val counter = new AtomicLong(0L)

      def newThread(r: Runnable) = {
        val th = new Thread(r)
        th.setName("srikanth-io-thread-" +
          counter.getAndIncrement.toString)
        th.setDaemon(true)
        th
      }
    })

  implicit val ec = ExecutionContext
    .fromExecutor(ioThreadPool)
  var input :Map[String,Any] = new HashMap[String,Any]()
  input += ("csvFile"->"/home/srikanth/drives/B/poc/earthquake/all_month.csv")
  input += ("delimiter"-> ",")
  input += ("tablename"-> "allmonths")
  /*implicit val ec = ExecutionContext.fromExecutorService {
    Executors.newFixedThreadPool(8)
  }*/
  val binding = new Router[Map[String,Any]].run(REQUEST("CREATEDB",input))
  println("bitool")
  async {
    await(binding)
  }
}

