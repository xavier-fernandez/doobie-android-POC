package org.doobie.view

import android.content.Context
import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}
import com.fortysevendeg.mvessel.AndroidDriver
import doobie.imports._
import org.doobie.view.DatabaseOpenHelper._

case class DatabaseOpenHelper(context: Context)
  extends SQLiteOpenHelper(context, databaseName, null, databaseVersion) {

  lazy val xa: Transactor[IOLite] = {
    val sqliteDatabase = getReadableDatabase
    AndroidDriver.register()

    DriverManagerTransactor[IOLite](
      driver = "com.fortysevendeg.mvessel.AndroidDriver",
      url = s"jdbc:sqlite:$databaseName"
    )
  }
  val driverName = "com.fortysevendeg.mvessel.AndroidDriver"

  override def onCreate(sqliteDatabase: SQLiteDatabase): Unit = {}

  override def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int): Unit =
    throw new IllegalStateException("Can't make an upgrade")
}

object DatabaseOpenHelper {

  val databaseName = "doobie-android.db"

  val databaseVersion = 1
}
