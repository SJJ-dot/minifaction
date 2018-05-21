package com.sjj.fiction

import com.sjj.fiction.model.Result
import com.sjj.fiction.util.toResult
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun handleControllerException(ex: Throwable): Result<Any> {
        ex.printStackTrace(System.err)
        return ex.toResult()
    }
}