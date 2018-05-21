package com.sjj.fiction.model

class Result<T>(val status: Int, val data: T? = null, val errorMsg: String? = null) {
    constructor(data: T) : this(1, data)
    constructor(msg: String?, detail: String? = msg) : this(-1, errorMsg = msg)
}