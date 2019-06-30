package ru.skillbranch.devintensive.utils

import java.util.*

object Utils {
    fun parseFullName(fullName:String?) : Pair<String?, String?> {
         val mFullName = fullName?.replace(Regex(" {2,}")," ")
        val parts : List<String>? = if (mFullName.isNullOrBlank()) null else mFullName.split(" ")

        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        //return Pair(firstName, lastName)
        return (if (firstName.isNullOrBlank()) null else firstName) to (if (lastName.isNullOrBlank()) null else lastName)
    }

    fun transliteration(payload: String, divider: String=" "): String {
        fun transliterateChar(char:String):String {
            return when (char) {
                "а" -> "a"
                "б" -> "b"
                "в" -> "v"
                "г" -> "g"
                "д" -> "d"
                "е" -> "e"
                "ё" -> "e"
                "ж" -> "zh"
                "з" -> "z"
                "и" -> "i"
                "й" -> "i"
                "к" -> "k"
                "л" -> "l"
                "м" -> "m"
                "н" -> "n"
                "о" -> "o"
                "п" -> "p"
                "р" -> "r"
                "с" -> "s"
                "т" -> "t"
                "у" -> "u"
                "ф" -> "f"
                "х" -> "h"
                "ц" -> "c"
                "ч" -> "ch"
                "ш" -> "sh"
                "щ" -> "sh'"
                "ъ" -> ""
                "ы" -> "i"
                "ь" -> ""
                "э" -> "e"
                "ю" -> "yu"
                "я" -> "ya"
                else -> divider
            }
        }

        var result = ""
        for (char in payload){
            result += if(char.isUpperCase()) transliterateChar(char.toLowerCase().toString()).toUpperCase(Locale("en"))
                      else transliterateChar(char.toString())
        }

        return result
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        fun getInitial(word:String?) = word?.trim()?.toUpperCase(Locale("ru"))?.getOrNull(0)

        val firstInitial = getInitial(firstName)
        val lastInitial = getInitial(lastName)
        return if (firstInitial==null && lastInitial==null) null else "${firstInitial ?: ""}${lastInitial ?: ""}"
    }
}