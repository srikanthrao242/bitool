package org.com.bitool.UI

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.{ExecutorService, Executors, ThreadFactory}

import com.bitool.com.MessageTypes.REQUEST
import org.com.bitool.server.Router

import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scalafx.event.ActionEvent
import scalafx.scene.control.{Label, TextField}
import scalafx.scene.layout.HBox
import scalafx.stage.FileChooser
import scalafxml.core.macros.sfxml

/**
  * Created by srikanth on 4/28/18.
  */
@sfxml
class DatasetUpload(private val tablename : TextField,
                    private val filepath : TextField,
                    private val hbox : HBox,
                    private val uploadlabel: Label) {
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

  def extrachFile(event: ActionEvent): Unit ={
    val fc = new FileChooser
    val selectedFile = fc.showOpenDialog(null)
    if(selectedFile != null){
      filepath.setText(selectedFile.toString)
    }

  }

  def saveParquet(event: ActionEvent): Unit ={
    if(filepath.getText == null || tablename.getText == null){
      if(filepath.getText == null){
        uploadlabel.setText("Please Choose A File")
      }
      if(tablename.getText == null){
        uploadlabel.setText("Please Give your Table Name")
      }
    }else if(StaticVariables.DATASETNAMES.contains(tablename.text.toString())) uploadlabel.setText("Table Name already exist")
    else{
      this.fileUpload(filepath.text(),tablename.text())
    }
  }

  def fileUpload(filePath:String,tableName:String): Unit ={
    var req: mutable.HashMap[String, Any] = new mutable.HashMap[String,Any]()
    req += ("tablename" -> tableName)
    req += ("csvFile" -> filePath)
    req += ("delimiter"-> ",")
    new Router[mutable.HashMap[String,Any]].run(REQUEST("CREATEDB",req))
  }


}
