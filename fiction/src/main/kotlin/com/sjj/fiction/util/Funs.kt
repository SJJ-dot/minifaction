package com.sjj.fiction.util

import java.util.regex.Pattern

fun String.domain(): String? {
    val pattern = "(http(s)?://[a-zA-z\\d.]++)/?"
    val r = Pattern.compile(pattern)
    val m = r.matcher(this)
    if (m.find()) {
        return m.group(1)
    }
    return null
}