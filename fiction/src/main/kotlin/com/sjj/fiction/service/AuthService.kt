package com.sjj.fiction.service

import com.sjj.fiction.model.Auth
import com.sjj.fiction.model.Result
import com.sjj.fiction.model.WeChatLogin
import com.sjj.fiction.source.remote.HttpSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

@Service
class AuthService {
    @Autowired
    private lateinit var httpSource: HttpSource
    @Autowired
    private lateinit var auth: Auth

    fun loginWeChat(code: String, secret: String): Mono<String> {
        return httpSource.loginWeChat(code, secret).map {
            if (it.errcode != 0) {
                throw Exception(it.errmsg)
            }
            if (!auth.open || auth.agree.contains(it.openid)) {
                return@map "success"
            }
            if (!auth.reject.contains(it.openid)) {
                auth.reject.add(it.openid)
                saveAuthToDisk()
            }
            throw Exception("No permission to access this interface")
        }
    }

    fun query(): Mono<Auth> {
        return Mono.just(auth)
    }

    fun add(id: String): Mono<Auth> {
        if (!auth.agree.contains(id) && auth.reject.contains(id)) {
            saveAuthToDisk()
        }
        return Mono.just(auth)
    }

    @Synchronized
    private fun saveAuthToDisk() {
        var stream: OutputStream? = null;
        try {
            stream = FileOutputStream(File("application.properties"))
            val properties = Properties();
            auth.agree.forEachIndexed { index, s ->
                properties.setProperty("agree[$index]", s)
            }
            auth.reject.forEachIndexed { index, s ->
                properties.setProperty("reject[$index]", s)
            }
            properties.store(stream, "auth list")
        } finally {
            stream?.close()
        }
    }


}