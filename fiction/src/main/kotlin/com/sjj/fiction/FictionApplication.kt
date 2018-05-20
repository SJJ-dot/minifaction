package com.sjj.fiction

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FictionApplication

fun main(args: Array<String>) {
    runApplication<FictionApplication>(*args)
}