package org.com.bitool.UI

import scalafx.scene.control.{MenuBar, MenuItem}
import scalafx.scene.layout.VBox
import scalafxml.core.macros.sfxml

/**
  * Created by srikanth on 5/1/18.
  */
@sfxml
class MenuController(private val vBox: VBox,
                     private val menuBar:MenuBar,
                     private val delimeterUpload : MenuItem,
                     private val excelUpload : MenuItem,
                     private val dbbUpload : MenuItem) {

  def loadUploadPanelDel(): Unit ={
    Util.loadFXML(getClass.getResource("/fxml/DatasetUpload.fxml"))
  }
  def loadUploadPanelExcel(): Unit ={

  }
  def loadUploadPanelDB(): Unit ={

  }
  def openFile(): Unit ={

  }
  def saveAnalytics(): Unit ={

  }
  def saveAsAnalytics(): Unit ={

  }
  def createTab(): Unit ={

  }
  def processExit(): Unit ={

  }

}
