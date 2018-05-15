package org.com.bitool.UI

import java.io.IOException
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{NoDependencyResolver, FXMLView}

/**
  * Created by srikanth on 4/22/18.
  */
object BI_Init extends JFXApp{
  val resource = getClass.getResource("/fxml/Login.fxml")
  if (resource == null) {
    throw new IOException("Cannot load resource: Login.fxml")
  }

  val root = FXMLView(resource, NoDependencyResolver)

  stage = new PrimaryStage() {
    title = "BITOOL"
    scene = new Scene(root)
  }
}
