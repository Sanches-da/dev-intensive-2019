package ru.skillbranch.devintensive.extensions

fun String.truncate(len:Int=16):String{
    val newValue = this.trimEnd()
    return newValue.take(len+1).trimEnd() + if (newValue.length > len) "..." else ""
}

fun String.stripHtml():String{
    return this
        .replace(Regex("(=(.*?)(\"(.*?)(?<!\\\\)\"|'(.*?)(?<!\\\\)').*?)"),"")
        .replace("&nbsp;"," ")
        .replace(Regex("&(.*?);"),"")
        .replace(Regex("<[^>]*>"),"")
        .replace("\n","")
        .replace(Regex(" {2,}")," ")
}