package com.sjj.fiction.util

import com.sjj.fiction.model.Result

fun <T> T.toResult(): Result<T> {
    if (this is Exception) {
        return Result(msg = message)
    }
    return Result(this)
}