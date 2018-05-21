package com.sjj.fiction.service

import com.sjj.fiction.model.Book
import com.sjj.fiction.model.BookGroup
import com.sjj.fiction.source.remote.AszwFictionSource
import com.sjj.fiction.source.remote.FictionSourceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class FictionService {
    @Autowired
    lateinit var fictionSources: List<FictionSourceInterface>

    fun search(searchKey: String): Flux<Book> {
//       return Flux.merge(fictionServices.map { it.search(name) })
        return Flux.create { emitter ->
            if (searchKey.isEmpty()) {
                throw IllegalArgumentException("搜索内容不能为空")
            }
            var count = fictionSources.size
            var error: Throwable? = null
            var hasData = false
            fictionSources.forEach {
                it.search(searchKey).subscribe({
                    emitter.next(it)
                    hasData = true
                }, {
                    if (--count == 0) {
                        if (hasData) {
                            emitter.complete()
                        } else {
                            emitter.error(it)
                        }
                    }
                    error = it
                }, {
                    if (--count == 0) {
                        if (hasData||error == null) {
                            emitter.complete()
                        } else {
                            emitter.error(error!!)
                        }
                    }
                })
            }
        }
    }
}