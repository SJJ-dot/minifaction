package com.sjj.fiction.controller

import com.sjj.fiction.model.Auth
import com.sjj.fiction.model.Result
import com.sjj.fiction.service.AuthService
import com.sjj.fiction.util.toResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

@RestController
class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @RequestMapping(value = "/auth", method = [RequestMethod.GET])
    fun auth(code: String, version: String?): Mono<Result<String>> {
        return authService.loginWeChat(code, version).toResult()
    }

    @RequestMapping(value = "/query", method = [RequestMethod.GET])
    fun query(): Mono<Result<Auth>> {
        return authService.query().toResult()
    }

    @RequestMapping(value = "/add", method = [RequestMethod.GET])
    fun add(id: String): Mono<Result<Auth>> {
        return authService.add(id).toResult()
    }

}