package com.sjj.fiction.model

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "auth")
@Component
class Auth {
    val agree = mutableSetOf<String>()
    val reject = mutableSetOf<String>()
}