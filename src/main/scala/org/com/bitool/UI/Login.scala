package org.com.bitool.UI

import scalafx.event.ActionEvent
import scalafx.scene.Node
import scalafx.scene.control.{PasswordField, TextField}
import scalafxml.core.macros.sfxml

/**
  * Created by srikanth on 4/23/18.
  */
@sfxml
class Login(private val username: TextField,
            private val password: PasswordField){

  def verifyUser(event: ActionEvent){
    /*var source : Node = event.getSource.asInstanceOf[Node]
    var stage = source.getScene.getWindow
    stage.getOnCloseRequest*/
    Util.loadFXML(getClass.getResource("/fxml/Main.fxml"))
  }

  def resetFields(event: ActionEvent){
    username.text = ""
    password.text = ""
  }

}
