package controllers

import play.api.mvc._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Enumerator
import play.api.libs.ws.WS
import models.NatalieDeeData

object Application extends Controller {

  def index = Action.async {
    val elementList = Future {
      val els = NatalieDeeData.getRandomImages

      Ok(views.html.index(els))
    }

    elementList
  }

  def random = Action.async {
    val elementList = Future {
      val els = NatalieDeeData.getRandomImages

      Ok(views.html.random(els))
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