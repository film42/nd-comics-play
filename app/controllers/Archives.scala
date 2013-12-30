package controllers

import play.api.mvc._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import java.util.Calendar
import models.NatalieDeeData

object Archives extends Controller {

  def archivesHome = Action {
    val years = Range(2008, Calendar.getInstance().get(Calendar.YEAR))
    val months = Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    Ok(views.html.archiveHome(years, months))
  }

  def archivesSelect(year: String, month: String) = Action.async {
    val elementList = Future {
      val els = NatalieDeeData.getArchiveImages(year, month)

      Ok(views.html.archiveSelection(els, year, month))
    }

    elementList
  }


}