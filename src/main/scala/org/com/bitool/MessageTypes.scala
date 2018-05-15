package org.com

/**
  * Created by srikanth on 4/28/18.
  */
package object bitool {
  case class REQUEST[A](name:String,class_obj:A)
  case class RESPONCE[A](name:String,class_obj:A)
}
