package com.sjj.fiction.model

class Result<T>(val status: Int, val data: T? = null, val msg: String? = null, val detail: String? = msg) {
    constructor(data: T) : this(1, data)
    constructor(msg: String?, detail: String? = msg) : this(-1, msg = msg, detail = detail)
}