package com.sjj.fiction.service

import com.sjj.fiction.model.Book
import com.sjj.fiction.source.remote.FictionSourceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class FictionService {
    @Autowired
    lateinit var fictionServices: List<FictionSourceInterface>

    fun search(name: String): Flux<Book> {
        return Flux.concatDelayError {
            fictionServices.forEach { fs->
                it.onNext(fs.search(name))
            }
        }
//        return fictionServices[1].search(name)
    }
}