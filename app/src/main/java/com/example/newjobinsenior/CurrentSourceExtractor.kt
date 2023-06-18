import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class CurrentSourceExtractor(private val pageUrl: String?, private val onCurrentSourceExtracted: (String?) -> Unit) {

    fun extractCurrentSource() {
        Thread {
            try {
                val document: Document = Jsoup.connect(pageUrl).get()
                val imageElement = findDesiredImageElement(document)
                val currentSource = extractCurrentSourceFromElement(imageElement)
                onCurrentSourceExtracted(currentSource)
            } catch (e: Exception) {
                e.printStackTrace()
                onCurrentSourceExtracted(null)
            }
        }.start()
    }

    private fun findDesiredImageElement(document: Document): Element? {
        return document.selectFirst("img[src*=content_img]")
    }

    private fun extractCurrentSourceFromElement(imageElement: Element?): String? {
        return imageElement?.attr("src")
    }
}
