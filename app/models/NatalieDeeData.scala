package models

import org.jsoup.nodes.{Document, Element}
import org.jsoup.Jsoup

object NatalieDeeData {

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

}
