package org.com.bitool.UI

import java.io.IOException
import java.net.URL

import scalafx.scene.Scene
import scalafx.stage.Stage
import scalafxml.core.{FXMLView, NoDependencyResolver}
import scalafx.Includes._

/**
  * Created by srikanth on 5/1/18.
  */
object Util {
    def loadFXML(resource:URL){
      //val resource: URL = getClass.getResource(fxmlpath)
      if (resource == null) {
        throw new IOException("Cannot load resource: "+resource.toString)
      }
      val root = FXMLView(resource, NoDependencyResolver)
      val stage = new Stage()
      stage.setTitle("BITOOL")
      stage.setScene(new Scene(root))
      stage.setResizable(false)
      stage.setMaximized(false)
      stage.show()
    }
}
