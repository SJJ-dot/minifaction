package com.sjj.fiction.source.remote

import com.sjj.fiction.model.Book
import com.sjj.fiction.model.Chapter
import org.jsoup.Jsoup
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URLEncoder

@Repository
class DhzwFictionSource : FictionSourceInterface {
    override val baseUrl: String = "https://www.dhzw.org"
    override fun search(search: String): Flux<Book> {
        return Flux.create { fs ->
            val document = Jsoup.connect("$baseUrl/modules/article/search.php?searchkey=${URLEncoder.encode(search, "gbk")}").get()
            val elementsByClass = document.body().getElementById("newscontent").getElementsByTag("ul")[0].getElementsByTag("li")
            elementsByClass.forEach {
                val ahref = it.child(1).select("a[href]")[0]
                fs.next(Book(ahref.attr("href"), ahref.text(), it.child(3).child(0).text()))
            }
            fs.complete()
        }
    }

    override fun chapter(url: String): Mono<Chapter> {
        return Mono.create {
            val document = Jsoup.connect(url).get()
            val parse = document.getElementById("BookText")
            val chapter = Chapter()
            chapter.url = url
            chapter.chapterName = document.getElementsByClass("bookname")[0].child(0).text()
            chapter.content = parse.html()
            it.success(chapter)
        }
    }

    override fun intro(url: String): Mono<Book> {
        return Mono.create {
            val parse = Jsoup.connect(url).get().body()
            val book = Book()
            book.url = url
            val infotitle = parse.getElementsByClass("infotitle")[0]
            book.name = infotitle.child(0).text()
            book.author = infotitle.child(1).text().split("：").last()
            book.bookCoverImgUrl = parse.getElementById("fmimg").select("[src]")[0].attr("src")
            book.intro = parse.getElementById("info").child(1).text()
            book.chapterList = parse.getElementById("list").select("a[href]").mapIndexed { index, e -> Chapter(e.attr("abs:href"),e.text()) }
            it.success(book)
        }
    }
}