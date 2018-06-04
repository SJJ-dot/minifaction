package com.sjj.fiction.service

import com.sjj.fiction.model.Book
import com.sjj.fiction.model.Chapter
import com.sjj.fiction.model.GBook
import com.sjj.fiction.source.remote.FictionSourceInterface
import com.sjj.fiction.util.domain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class FictionService {
    @Autowired
    lateinit var fictionSources: List<FictionSourceInterface>

    fun search(searchKey: String): Mono<List<GBook>> {
//       return Flux.merge(fictionServices.map { it.search(name) })
        return Mono.create { emitter ->
            if (searchKey.isEmpty()) {
                emitter.error(IllegalArgumentException("搜索内容不能为空"))
                return@create
            }
            var count = fictionSources.size
            var error: Throwable? = null
            val result = mutableMapOf<String,MutableList<Book>>()
            fictionSources.forEach {
                it.search(searchKey).subscribe({
                    result.getOrPut(it.name+it.author){ mutableListOf()}.add(it)
                }, {
                    if (--count == 0) {
                        if (result.isNotEmpty()) {
                            emitter.success(result.map { GBook(it.value.first().name,it.value.first().author,it.value) })
                        } else {
                            emitter.error(it)
                        }
                    }
                    error = it
                    it.printStackTrace()
                }, {
                    if (--count == 0) {
                        if (result.isNotEmpty() || error == null) {
                            emitter.success(result.map { GBook(it.value.first().name, it.value.first().author, it.value) })
                        } else {
                            emitter.error(error!!)
                        }
                    }
                })
            }
        }
    }

    fun intro(url: String): Mono<Book> {
        return Mono.create {
            it.onDispose(getSource(url).intro(url).subscribe(it::success, it::error))
        }
    }

    fun chapter(url: String): Mono<Chapter> {
        return Mono.create {
            it.onDispose(getSource(url).chapter(url).subscribe(it::success, it::error))
        }
    }

    private fun getSource(url: String): FictionSourceInterface {
        return fictionSources.find { url.domain() == it.baseUrl.domain() }
                ?: throw Exception("未找到小说资源站：${url.domain()}")
    }

}