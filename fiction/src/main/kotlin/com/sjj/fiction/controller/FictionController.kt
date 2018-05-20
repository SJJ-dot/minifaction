package com.sjj.fiction.controller

import com.sjj.fiction.model.Book
import com.sjj.fiction.service.FictionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class FictionController {
    @Autowired
    lateinit var fictionService:FictionService
    @RequestMapping(value = "/", method = [RequestMethod.GET])
    fun default() = "hello everyone"

    @RequestMapping(value = "/{name}", method = [RequestMethod.GET])
    fun search(@PathVariable name: String): Flux<Book> {
        return fictionService.search(name)
    }

}