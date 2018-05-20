package com.sjj.fiction.source.remote

import com.sjj.fiction.model.Book
import com.sjj.fiction.model.Chapter
import org.jsoup.Jsoup
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.net.URLEncoder

@Repository
class BiqugeFictionSource : FictionSourceInterface {
    override val baseUrl = "https://www.biquge5200.cc"
    override fun search(search: String): Flux<Book> {
        return Flux.create { fl ->
            val document = Jsoup.connect("$baseUrl/modules/article/search.php?searchkey=$search").get()
            val children = document.body().getElementsByTag("tbody")[0].children()
            children.takeLast(children.size - 1).forEach {
                val element = it.select("a[href]")[0]
                fl.next(Book(element.attr("href"), element.text(), it.child(2).text()))
            }
            fl.complete()
        }
    }

    override fun loadBookChapter(chapter: Chapter): Flux<Chapter> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBookDetailsAndChapterList(book: Book): Flux<Book> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}