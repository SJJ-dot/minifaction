package com.sjj.fiction.source.remote

import com.sjj.fiction.model.Book
import com.sjj.fiction.model.Chapter
import org.jsoup.Jsoup
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URLEncoder

@Repository
class YunlaigeFictionSource : FictionSourceInterface {
    override val baseUrl: String = "http://www.yunlaige.com"

    override fun search(search: String): Flux<Book> {
        return Flux.create { fs ->
            val document = Jsoup.connect("$baseUrl/modules/article/search.php?searchkey=${URLEncoder.encode(search, "gbk")}").get()
            try {
                document.body().getElementsByClass("chart-dashed-list")[0].children().forEach {
                    val child1 = it.child(1).child(0).child(0).select("a[href]")[0]
                    val book = Book(child1.attr("href"), child1.text(), it.child(1).child(1).text().split("/")[0])
                    fs.next(book)
                }
                fs.complete()
            } catch (e: Exception) {
                val element = document.body().getElementsByClass("book-info")[0]
                val info = element.getElementsByClass("info")[0]
                val name = info.child(0).child(0).text()
                val author = info.child(1).child(0).text()
                val split = document.getElementsByClass("book-info")[0].child(0).attr("href").split("/")
                fs.next(Book("$baseUrl/book/${split[split.size - 2]}.html", name, author))
                fs.complete()
            }
        }
    }

    override fun chapter(url: String): Mono<Chapter> {
        return Mono.create {
            val document = Jsoup.connect(url).get()
            val element = document.getElementById("content")
            val chapter = Chapter()
            chapter.url = url
            chapter.chapterName = document.getElementById("PagePosition1_lab_Current").text()
            chapter.content =element.html()
            it.success(chapter)
        }
    }

    override fun intro(url: String): Mono<Book> {
        return Mono.create {
            val element = Jsoup.connect(url).get().body().getElementsByClass("book-info")[0]
            val info = element.getElementsByClass("info")[0]
            val book = Book()
            book.url = url
            book.name = info.child(0).child(0).text()
            book.author = info.child(1).child(0).text()
            book.bookCoverImgUrl = element.select("a[href]")[0].attr("href")
            book.intro = info.child(2).text()
            val chapterListUrl = info.child(3).select("a[href]")[0].attr("href")
            book.chapterList = Jsoup.connect(chapterListUrl).get().getElementById("contenttable").child(0).select("a[href]").mapIndexed { index, e ->
                Chapter(e.attr("abs:href"), e.text())
            }
            it.success(book)
        }
    }
}