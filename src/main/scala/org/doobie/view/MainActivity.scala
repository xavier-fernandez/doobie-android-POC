package org.doobie.view

import android.os.Bundle
import android.support.v4.app.{Fragment, FragmentActivity}
import android.widget.{Button, LinearLayout, TextView}
import macroid.FullDsl._
import macroid._
import macroid.contrib.TextTweaks
import doobie.imports._
import cats._, cats.data._, cats.implicits._
import fs2.interop.cats._

class MainActivity
  extends FragmentActivity
    with Contexts[FragmentActivity] {

  //***********************************************
  //*************    UI CONNECTORS   **************
  //***********************************************
  private[this] var sampleTextView = slot[TextView]

  //***********************************************
  //**********    DATABASE TRANSACTOR    **********
  //***********************************************

  private[this] def xa(implicit ctx: ContextWrapper) = {
    new DatabaseOpenHelper(getApplicationContext).xa
  }

  //***********************************************
  //*************      APP LOGIC     **************
  //***********************************************

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    setContentView {
      //
      Ui.get {
        layout[LinearLayout](
          widget[Button] <~ text("Click me") <~ On.click { sampleTextView <~ show },
          widget[TextView] <~ wire(sampleTextView) <~ sampleDbQuery()
        ) <~ vertical
      }
    }
  }

  def sampleDbQuery()(implicit ctx: ContextWrapper): Tweak[TextView] = {
    val queryResult : String  =
      sql"select 42"
        .query[Int]
        .unique
        .transact(xa)
        .unsafePerformIO
          .toString

    TextTweaks.large + text(queryResult.toString) + hide
  }
}
