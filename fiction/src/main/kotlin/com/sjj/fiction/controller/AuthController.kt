package com.sjj.fiction.controller

import com.sjj.fiction.model.Result
import com.sjj.fiction.service.AuthService
import com.sjj.fiction.util.toResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @RequestMapping(value = "/auth", method = [RequestMethod.GET])
    fun auth(code: String, secret: String?): Mono<Result<String>> {
        return authService.loginWeChat(code, secret ?: "ef1b6c497ddd5d0e21410b459236ef38").toResult()
    }

    @RequestMapping(value = "/query", method = [RequestMethod.GET])
    fun query(): Mono<Result<Set<String>>> {
        return authService.query().toResult()
    }

    @RequestMapping(value = "/add", method = [RequestMethod.GET])
    fun add(id: String): Mono<Result<Set<String>>> {
        return authService.add(id).toResult()
    }
}