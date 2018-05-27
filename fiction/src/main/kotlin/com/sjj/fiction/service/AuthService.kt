package com.sjj.fiction.service

import com.sjj.fiction.model.Result
import com.sjj.fiction.model.WeChatLogin
import com.sjj.fiction.source.remote.HttpSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService {
    @Autowired
    private lateinit var httpSource: HttpSource

    private val authSet = mutableSetOf<String>()

    private val failedSet = mutableSetOf<String>()

    fun loginWeChat(code: String, secret: String): Mono<String> {
        return httpSource.loginWeChat(code, secret).map {
            if (it.errcode != 0) {
                throw Exception(it.errmsg)
            }
            if (authSet.contains(it.openid)) {
                return@map "success"
            }
            failedSet.add(it.openid)
            throw Exception("No permission to access this interface")
        }
    }

    fun query(): Mono<Set<String>> {
        return Mono.just(failedSet)
    }

    fun add(id: String): Mono<Set<String>> {
        if (failedSet.remove(id)) {
            authSet.add(id)
        }
        return Mono.just(authSet)
    }
}