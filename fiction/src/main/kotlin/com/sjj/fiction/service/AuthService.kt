package com.sjj.fiction.service

import com.sjj.fiction.model.Auth
import com.sjj.fiction.source.remote.HttpSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService {
    @Autowired
    private lateinit var httpSource: HttpSource
    @Autowired
    private lateinit var auth: Auth

    @Value("\${wechat.secret}")
    private lateinit var secret:String

    fun loginWeChat(code: String, version: String?): Mono<String> {
        return httpSource.loginWeChat(code, secret).map {
            if (it.errcode != 0) {
                throw Exception(it.errmsg)
            }
            if (auth.ignoreVersions.contains(version) || auth.agree.contains(it.openid)) {
                return@map "success"
            }
            if (!auth.reject.contains(it.openid)) {
                auth.reject.add(it.openid)
                auth.saveAuthToDisk()
            }
            throw Exception("No permission to access this interface")
        }
    }

    fun query(): Mono<Auth> {
        return Mono.just(auth)
    }

    fun add(id: String): Mono<Auth> {
        if (!auth.agree.contains(id) && auth.reject.contains(id)) {
            auth.saveAuthToDisk()
        }
        return Mono.just(auth)
    }




}