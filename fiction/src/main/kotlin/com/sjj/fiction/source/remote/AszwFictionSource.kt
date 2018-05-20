package com.sjj.fiction.source.remote

import com.sjj.fiction.model.Book
import com.sjj.fiction.model.Chapter
import org.jsoup.Jsoup
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.net.URL
import java.net.URLEncoder

@Repository
class AszwFictionSource : FictionSourceInterface {
    override val baseUrl = "https://www.aszw.org"
    override fun search(search: String): Flux<Book> {
        return Flux.create {
            val document = Jsoup.connect("$baseUrl/modules/article/search.php?searchkey=${URLEncoder.encode(search,"gbk")}").get()
            val body = document.body()
            val content = body.getElementById("content")
            val element = content.getElementsByTag("tbody")[0].getElementsByTag("tr")
            for (i in 1 until element.size) {
                val select = element[i].select("a[href]")
                val url = select[0].attr("href")
                val name = select[0].text()
                val author = select[2].text()
                it.next(Book(url, name, author))
            }
            it.complete()
        }
    }

    override fun loadBookChapter(chapter: Chapter): Flux<Chapter> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBookDetailsAndChapterList(book: Book): Flux<Book> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}