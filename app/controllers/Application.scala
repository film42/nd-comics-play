package controllers

import play.api.mvc._
import org.jsoup._
import org.jsoup.nodes.Element
import org.jsoup.nodes.Document
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.net.URL
import java.io.{FileOutputStream, File}
import play.api.libs.iteratee.Enumerator
import play.api.libs.ws.WS
import play.api.libs.ws.Response

object Application extends Controller {

  def getRandomImages: Array[Element] = {
    val url = "http://localhost:9000/assets/output.html"
    val doc: Document = Jsoup.connect(url).get()
    val list = doc.select(".arcDayComic img.comic").toArray

    list.map(x => x.asInstanceOf[Element])
  }

  def index = Action {
      Ok(views.html.index())
  }

  def specific = Action.async {
    val elementList = Future {
      val els = getRandomImages

      println("Out: " + els(0).attr("http"))

      Ok(views.html.specific(els))
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