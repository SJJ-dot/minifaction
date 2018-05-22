package com.sjj.fiction.service

import com.sjj.fiction.model.Book
import com.sjj.fiction.model.Chapter
import com.sjj.fiction.source.remote.FictionSourceInterface
import com.sjj.fiction.util.domain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class FictionService {
    @Autowired
    lateinit var fictionSources: List<FictionSourceInterface>

    fun search(searchKey: String): Mono<List<Book>> {
//       return Flux.merge(fictionServices.map { it.search(name) })
        return Mono.create { emitter ->
            if (searchKey.isEmpty()) {
                emitter.error(IllegalArgumentException("搜索内容不能为空"))
                return@create
            }
            var count = fictionSources.size
            var error: Throwable? = null
            var hasData = false
            val result = mutableListOf<Book>()
            fictionSources.forEach {
                it.search(searchKey).subscribe({
                    result.add(it)
                    hasData = true
                }, {
                    if (--count == 0) {
                        if (hasData) {
                            emitter.success(result)
                        } else {
                            emitter.error(it)
                        }
                    }
                    error = it
                    it.printStackTrace()
                }, {
                    if (--count == 0) {
                        if (hasData || error == null) {
                            emitter.success(result)
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