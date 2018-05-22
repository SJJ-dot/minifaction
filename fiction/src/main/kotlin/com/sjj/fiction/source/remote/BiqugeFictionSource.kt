package com.sjj.fiction.source.remote

import com.sjj.fiction.model.Book
import com.sjj.fiction.model.Chapter
import org.jsoup.Jsoup
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
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

    override fun chapter(url: String): Mono<Chapter> {
        return Mono.create<Chapter> {
            val get = Jsoup.connect(url).get()
            val parse = get.getElementById("content")
            val chapter = Chapter()
            chapter.url = url
            chapter.chapterName =get.getElementsByClass("bookname")[0].child(0).text()
            chapter.content = parse.html()
            it.success(chapter)
        }
    }

    override fun intro(url: String): Mono<Book> {
        return Mono.create{
            val parse = Jsoup.connect(url).get().body()
            val book = Book()
            book.url = url
            val info = parse.getElementById("info")
            book.name = info.child(0).text()
            book.author = info.child(1).text().trim().split("：").last()
            book.bookCoverImgUrl = parse.getElementById("fmimg").select("[src]")[0].attr("src")
            book.intro = parse.getElementById("intro").child(0).text()
            val children = parse.getElementById("list").child(0).children()
            val last = children.indexOfLast { it.tag().name == "dt" }
            book.chapterList = children.subList(last+1,children.size)
                    .map { it.select("a[href]") }
                    .mapIndexed { index, e -> Chapter(e.attr("abs:href"), chapterName = e.text()) }
            it.success(book)
        }
    }
}