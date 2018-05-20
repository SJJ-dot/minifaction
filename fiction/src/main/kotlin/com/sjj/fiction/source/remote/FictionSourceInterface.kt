package com.sjj.fiction.source.remote

import com.sjj.fiction.model.Book
import com.sjj.fiction.model.Chapter
import reactor.core.publisher.Flux

interface FictionSourceInterface {
    val baseUrl: String
    fun search(search: String): Flux<Book>
    fun loadBookChapter(chapter: Chapter): Flux<Chapter>
    fun loadBookDetailsAndChapterList(book: Book): Flux<Book>
}