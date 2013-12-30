package controllers

import play.api.mvc._
import org.jsoup._
import org.jsoup.nodes.Element
import org.jsoup.nodes.Document
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import play.api.libs.iteratee.Enumerator
import play.api.libs.ws.WS
import java.util.Calendar

object Application extends Controller {

  val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_0) AppleWebKit/537.36 " +
                  "(KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36"

  def getRandomImages: Array[Element] = {
    val url = "http://www.nataliedee.com/index.php"
    val doc: Document = Jsoup.connect(url).header("User-Agent", userAgent).get()
    val list = doc.select(".arcDayComic img.comic").toArray

    list.map(x => x.asInstanceOf[Element])
  }

  def getArchiveImages(year: String, month: String): Array[Element] = {
    val url = "http://www.nataliedee.com/archives/"+year+"/"+month+"/"
    val doc: Document = Jsoup.connect(url).header("User-Agent", userAgent).get()
    val list = doc.select(".arcDayComic img.comic").toArray

    list.map(x => x.asInstanceOf[Element])
  }

  def index = Action.async {
    val elementList = Future {
      val els = getRandomImages

      Ok(views.html.index(els))
    }

    elementList
  }

  def random = Action.async {
    val elementList = Future {
      val els = getRandomImages

      Ok(views.html.random(els))
    }

    elementList
  }

  def archivesHome = Action {
    val years = Range(2008, Calendar.getInstance().get(Calendar.YEAR))
    val months = Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    Ok(views.html.archiveHome(years, months))
  }

  def archivesSelect(year: String, month: String) = Action.async {
    val elementList = Future {
      val els = getArchiveImages(year, month)

      Ok(views.html.archiveSelection(els, year, month))
    }

    elementList
  }


  def proxy(url: String) = Action {
    val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_0) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36"

    val proxyResp = WS.url(url).withHeaders(("User-Agent", userAgent)).get()

    val result: Future[Result] = proxyResp.map { resp =>
      val stream = resp.ahcResponse.getResponseBodyAsStream
      Ok.chunked(Enumerator.fromStream(stream)).as(resp.ahcResponse.getContentType)
    }

    Async(result)
  }

}