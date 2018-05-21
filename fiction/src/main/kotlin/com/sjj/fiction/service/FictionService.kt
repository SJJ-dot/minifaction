package com.sjj.fiction.service

import com.sjj.fiction.model.Book
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
            val domain = url.domain()
            if (domain == null) {
                it.error(Exception("请求URL解析错误：$url"))
                return@create
            }
            val find = fictionSources.find { it.baseUrl.domain().equals(domain) }
            if (find == null) {
                it.error(Exception("未找到小说资源站：$domain"))
                return@create
            }
            it.onDispose(find.intro(url).subscribe(it::success,it::error))

        }
    }
}