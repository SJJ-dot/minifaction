package com.sjj.fiction.model

class Book(var url: String = "",
           var name: String = "",
           var author: String = "佚名",
           var bookCoverImgUrl: String = "",
           var intro: String = "",
           var chapterListUrl: String = "",
           var chapterList: List<Chapter> = mutableListOf()
)