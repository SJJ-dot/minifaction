package com.sjj.fiction.source.remote

import com.sjj.fiction.model.Book
import com.sjj.fiction.model.Chapter
import org.jsoup.Jsoup
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URL
import java.net.URLEncoder

@Repository
class AszwFictionSource : FictionSourceInterface {
    override val baseUrl = "https://www.aszw.org"
    override fun search(search: String): Flux<Book> {
        return Flux.create {
            val document = Jsoup.connect("$baseUrl/modules/article/search.php?searchkey=${URLEncoder.encode(search, "gbk")}").get()
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

    override fun chapter(url: String): Mono<Chapter> {
        return Mono.create<Chapter> {
            val get = Jsoup.connect(url).get()
            val parse = get.getElementById("contents")
            val chapter = Chapter()
            chapter.url = url
            chapter.chapterName =get.getElementsByClass("bdb")[0].text()
            chapter.content = parse.html()
            it.success(chapter)
        }
    }

    override fun intro(url: String): Mono<Book> {
        return Mono.create {
            val book = Book()
            book.url = url
            val body = Jsoup.connect(url).get().body()
            val parse = body.getElementsByClass("info")[0]
            val btitle = parse.getElementsByClass("btitle")
            book.name = btitle[0].child(0).text()
            book.author = btitle[0].child(1).text().trim().split("：").last()
            book.bookCoverImgUrl = parse.select("[src]")[0].attr("src")
            book.intro = parse.getElementsByClass("book")[0].getElementsByClass("js")[0].text()
            book.chapterList = body.getElementById("at").select("a[href]").mapIndexed { index, e -> Chapter(e.attr("abs:href"),chapterName = e.text()) }
            it.success(book)
        }
    }
}