package com.sjj.fiction.model

class GBook(var name: String = "",
            var author: String = "佚名",
            var books: MutableList<Book> = mutableListOf())