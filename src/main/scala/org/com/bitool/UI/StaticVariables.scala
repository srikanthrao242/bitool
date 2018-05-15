package org.com.bitool.UI

import scala.collection.immutable.HashMap
import scala.collection.mutable.ArrayBuffer

/**
  * Created by srikanth on 5/1/18.
  */
object StaticVariables {
  var DATASETNAMES: ArrayBuffer[String] =  new ArrayBuffer[String]()
  var CATEGORIES = new HashMap[String,ArrayBuffer[String]]()
  var CATEGORIESANDDATATPES = new HashMap[String,HashMap[String,String]]()
  var INSTANCE_ID = 0
}
