package org.com.bitool.server

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql._

import scala.collection.immutable.HashMap
import scala.collection.{Map, mutable}
import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util._
/**
  * Created by srikanth on 4/28/18.
  */
class DataBase{

  val config = ConfigFactory.load("/src/main/resources/SERVER/application.conf")

  def getDF_Delim(file : String,delimiter : String): Future[DataFrame] =async{
    var dataframe = ServerApp.SPARK.emptyDataFrame
    try{
      dataframe= ServerApp.SPARK.read
          .format("com.databricks.spark.csv")
          .option("header", "true") // Use first line of all files as header
          .option("inferSchema", "true")
          .option("delimiter",delimiter)
          .load(file)
    }catch {
      case e:Exception=> dataframe
    }
    dataframe
  }

  def createDataSourceInfo(): Future[Boolean] = async{

    val metaDB = config.getString("databases.metadb")
    var isCreated = false
    try{
      val dataframe = ServerApp.SPARK.sql("CREATE TABLE IF NOT EXISTS "
        +metaDB+".database_info ( db_id int, db_name String, "
        +" user_id int)")
      isCreated = true
    }catch {
      case e:Exception=> isCreated = false
    }
    isCreated
  }


  def saveDataFrameAsParquet(dataframe : DataFrame,tablename:String): Future[Boolean] = async{
    val mainDB = config.getString("databases.maindb")
    var isSaved = false
    try{
      dataframe.write.format("parquet").saveAsTable(mainDB+"."+tablename)
      isSaved = true
    }catch {
      case e:Exception=> false
    }
    isSaved
  }

  def insertPredicates(data : Map[String,Any],tableName: String) : Future[Boolean] = async{
    val metaDB = config.getString("databases.metadb")
    val spark = ServerApp.SPARK
    var isInserted = false
    try{
      data.foreach(f=>{
        if(f._2.toString.toLowerCase().contains("int")
          ||f._2.toString.toLowerCase().equals("float")
          ||f._2.toString.toLowerCase().equals("double")
          ||f._2.toString.toLowerCase().equals("decimal")
          ||f._2.toString.toLowerCase().contains("tinyint")
          ||f._2.toString.toLowerCase().contains("smallint")
          ||f._2.toString.toLowerCase().contains("bigint")){
          var queryString = "INSERT INTO TABLE "+metaDB+".numericpredicates  "
          queryString = queryString+"VALUES('"+tableName+"','"+f._1.toString+"','"+f._2.toString+"')"
          var res = spark.sql(queryString)
        }else if(f._2.toString.toLowerCase().equals("timestamp")){
          var queryString = "INSERT INTO TABLE "+metaDB+".timestamppredicates  "
          queryString = queryString+"VALUES('"+tableName+"','"+f._1.toString+"','"+f._2.toString+"')"
          var res = spark.sql(queryString)
        }else if(f._2.toString.toLowerCase().equals("date")){
          var queryString = "INSERT INTO TABLE "+metaDB+".datepredicates  "
          queryString = queryString+"VALUES('"+tableName+"','"+f._1.toString+"','"+f._2.toString+"')"
          var res = spark.sql(queryString)
        }else if(f._2.toString.toLowerCase().equals("boolean")){
          var queryString = "INSERT INTO TABLE "+metaDB+".booleanpredicates  "
          queryString = queryString+"VALUES('"+tableName+"','"+f._1.toString+"','"+f._2.toString+"')"
          var res = spark.sql(queryString)
        }else if(f._2.toString.toLowerCase().equals("binary")){
          var queryString = "INSERT INTO TABLE "+metaDB+".binarypredicates  "
          queryString = queryString+"VALUES('"+tableName+"','"+f._1.toString+"','"+f._2.toString+"')"
          var res = spark.sql(queryString)
        }else{
          var queryString = "INSERT INTO TABLE "+metaDB+".stringpredicates  "
          queryString = queryString+"VALUES('"+tableName+"','"+f._1.toString+"','"+f._2.toString+"')"
          var res = spark.sql(queryString)
        }
      })
      isInserted = true
    }catch{
      case e:Exception => {
        isInserted = false
      }
    }
    isInserted
  }
  def dropTable(data:Map[String,Any]):Future[Boolean] = async{
    val metaDB = config.getString("databases.metadb")
    val mainDB = config.getString("databases.maindb")
    val spark = ServerApp.SPARK
    var isDeleted = false
    try{
      val db_name = data.get("db_name").toString
      var queryString = "DELETE FROM metadb.database_info WHERE db_name="+db_name
      spark.sql(queryString)

      var intQuery_str = "DELETE FROM "+metaDB+".numericpredicates db_name="+db_name
      spark.sql(queryString)

      var timeQuery_str = "DELETE FROM "+metaDB+".timestamppredicates db_name="+db_name
      spark.sql(queryString)

      var dateQuery_str = "DELETE FROM "+metaDB+".datepredicates db_name="+db_name
      spark.sql(queryString)

      var boolQuery_str = "DELETE FROM "+metaDB+".booleanpredicates db_name="+db_name
      spark.sql(queryString)

      var binaryQuery_str = "DELETE FROM "+metaDB+".binarypredicates db_name="+db_name
      spark.sql(queryString)

      var strQuery_str = "DELETE FROM "+metaDB+".stringpredicates db_name="+db_name
      spark.sql(queryString)

      var dropTable_str = "DROP TABLE IF EXISTS "+mainDB+"."+db_name
      spark.sql(queryString)

      isDeleted = true

    }catch{
      case e:Exception => { isDeleted = false}
    }
    isDeleted
  }
  def insertDatasource_info(data : Map[String,Any]) : Future[Boolean] = async{
    var isInserted = false
    try{
      var queryString = "INSERT INTO TABLE metadb.database_info  "
      val db_id = data.get("db_id").asInstanceOf[Int]
      val db_name = data.get("db_name").toString
      val user_id = data.get("user_id").asInstanceOf[Int]
      queryString = queryString+"VALUES("+db_id+",'"+db_name+"',"+user_id+")"
      var res = ServerApp.SPARK.sql(queryString)
      isInserted = true
    }catch{
      case e:Exception => {isInserted = false}
    }
    isInserted
  }
  def createPredicatesTables():Future[Boolean]=async{
    val javaHashMap = new java.util.HashMap[String,Object]()
    val metaDB = config.getString("databases.metadb")
    val spark = ServerApp.SPARK
    var isCreated = false
    try{
      val stringDataframe = spark.sql("CREATE TABLE IF NOT EXISTS "
        +metaDB+".stringpredicates ( db_name String, "
        +"category String ,"
        +"type String )")
      val numericDataframe = spark.sql("CREATE TABLE IF NOT EXISTS "
        +metaDB+".numericpredicates ( db_name String, "
        +"category String ,"
        +"type String )")
      val timeStampDataframe = spark.sql("CREATE TABLE IF NOT EXISTS "
        +metaDB+".timestamppredicates ( db_name String, "
        +"category String ,"
        +"type String )")
      val dateDataframe = spark.sql("CREATE TABLE IF NOT EXISTS "
        +metaDB+".datepredicates ( db_name String, "
        +"category String ,"
        +"type String )")
      val booleanDataframe = spark.sql("CREATE TABLE IF NOT EXISTS "
        +metaDB+".booleanpredicates ( db_name String, "
        +"category String ,"
        +"type String )")
      val binaryDataframe = spark.sql("CREATE TABLE IF NOT EXISTS "
        +metaDB+".binarypredicates ( db_name String, "
        +"category String ,"
        +"type String )")
      isCreated = true
    }catch{
      case e:Exception =>{
        isCreated = false
      }
    }
    isCreated
  }
  def createMCTable(tableMap :Map[String,Any],tableName: String) : Future[Boolean] =async{
    var result :Map[String,Any] = new HashMap[String,Any]()
    var tableSchema  = tableMap
    var tablename = "MC_"+tableName
    val metaDB = config.getString("databases.metadb")
    val spark = ServerApp.SPARK
    var isCreated = false
    try
    {
      var tablesch = spark.sparkContext.makeRDD(tableSchema.toSeq)
      val dfRdd = tablesch.map {
        case (s0, s1) => Row(s0, s1.toString)}
      val schema = StructType(StructField("cat_Name", StringType, true)::
        StructField("dataType", StringType, true)::Nil)
      val df1 =  spark.createDataFrame(dfRdd, schema)
      import spark.sqlContext.implicits._
      df1.write.format("parquet").saveAsTable(metaDB+"."+tablename)
      isCreated = true
    }catch{
        case e : Exception => {
          isCreated = false
        }
    }
    isCreated
  }
  def createDatabases() :Future[Boolean]=async{
    var isCreated = false
    try{
      val mainDB = config.getString("databases.maindb")
      val metaDB = config.getString("databases.metadb")
      val databasesQuery = "CREATE DATABASE IF NOT EXISTS "+mainDB
      val metaDBQuery = "CREATE DATABASE IF NOT EXISTS "+metaDB
      ServerApp.SPARK.sql(databasesQuery)
      ServerApp.SPARK.sql(metaDBQuery)
      isCreated = true
    }catch{
        case e : Exception =>{isCreated = true       }
      }
    isCreated
  }
  def getSchemaMap (dataframe : DataFrame) : Map[String,Any] ={
    var result :Map[String,Any] = new HashMap[String,Any]()
    try {
      val dfMap = dataframe.schema
      var hashMap = new HashMap[String, Any]()
      dfMap.fields.foreach { x => hashMap += (x.name -> x.dataType.simpleString.toUpperCase()) }
      result += ("msg" -> "Success")
      result += ("hashMap" -> hashMap)
      result
    }catch {
      case e: Exception => {
        result += ("msg" -> "Error")
        result += ("hashMap" -> null)
        result += ("error" -> e.toString)
        result
      }
    }
  }
  def createTable (tableMap : Map[String,Any]):Future[Boolean] = async{
    val csvFile : String = tableMap("csvFile").toString
    val tableName = tableMap("tablename").toString
    val delimiter = tableMap("delimiter").toString
    var isSaved = false
    /*try
    {*/
      val dataFrame =  await(this.getDF_Delim(csvFile,delimiter))
      if(DataFrameExtensions.extendedDataFrame(dataFrame).nonEmpty()){
        val map = this.getSchemaMap(dataFrame)
        if(map("msg").toString.equals("Success")){
          val hmap = map("hashMap").asInstanceOf[HashMap[String,Any]]
          if(await(this.insertPredicates(hmap, tableName))){
            if(await(this.saveDataFrameAsParquet(dataFrame,tableName))){
              isSaved = true
            }
          }
        }
      }
   /* }catch{
        case e : Exception => isSaved = false
      }*/
    isSaved
  }
}

object DataFrameExtensions {
  implicit def extendedDataFrame(dataFrame: DataFrame): ExtendedDataFrame =
    new ExtendedDataFrame(dataFrame: DataFrame)

  class ExtendedDataFrame(dataFrame: DataFrame) {
    def isEmpty(): Boolean = {
      Try{dataFrame.first.length != 0} match {
        case Success(_) => false
        case Failure(_) => true
      }
    }

    def nonEmpty(): Boolean = !isEmpty
  }
}
