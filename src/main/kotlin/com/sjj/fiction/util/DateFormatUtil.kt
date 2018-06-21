package com.sjj.fiction.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * yyyy-MM-dd HH:mm:ss
 */
//val defSimpleDateFormat by lazy { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) }

val DATE_PATTERN_DEF = "yyyy-MM-dd HH:mm:ss"

fun String.formatDate(date: Any) = getDateFormat(this).format(date)

fun String.parseDate(date: String) = getDateFormat(this).parse(date)

private val threadLocal =object :ThreadLocal<MutableMap<String, SimpleDateFormat>>(){
    override fun initialValue(): MutableMap<String, SimpleDateFormat>  = mutableMapOf()
}

private fun getDateFormat(pattern: String): SimpleDateFormat {
    return threadLocal.get().getOrPut(pattern) { SimpleDateFormat(pattern, Locale.CHINESE) }
}