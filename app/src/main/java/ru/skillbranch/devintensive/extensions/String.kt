package ru.skillbranch.devintensive.extensions

fun String?.truncate(len:Int=16):String?{
    if (this == null) return null
    val newValue = this.trimEnd()
    return newValue.take(len).trimEnd() + if (newValue.length > len) "..." else ""
}

fun String?.stripHtml():String?{
    if (this == null) return null
    return this
        .replace(Regex("(=(.*?)(\"(.*?)(?<!\\\\)\"|'(.*?)(?<!\\\\)').*?)"),"")
        .replace("&nbsp;"," ")
        .replace(Regex("&(.*?);"),"")
        .replace(Regex("<[^>]*>"),"")
        .replace("\n","")
        .replace(Regex(" {2,}")," ")
}