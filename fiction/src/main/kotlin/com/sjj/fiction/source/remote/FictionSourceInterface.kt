package com.sjj.fiction.source.remote

import com.sjj.fiction.model.Book
import com.sjj.fiction.model.Chapter
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface FictionSourceInterface {
    val baseUrl: String
    fun search(search: String): Flux<Book>
    fun chapter(url: String): Mono<Chapter>
    fun intro(url: String): Mono<Book>
}