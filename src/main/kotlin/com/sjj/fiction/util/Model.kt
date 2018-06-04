package com.sjj.fiction.util

import com.sjj.fiction.model.Result
import reactor.core.publisher.Mono

fun <T> T.toResult(): Result<T> {
    if (this is Exception) {
        return Result(msg = message)
    }
    return Result(this)
}

fun <T> Mono<T>.toResult(): Mono<Result<T>> {
    return map {
        if (it is Exception) {
            return@map Result<T>(msg = it.message)
        }
        return@map Result(it)
    }
}