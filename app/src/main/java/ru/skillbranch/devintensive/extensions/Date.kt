package ru.skillbranch.devintensive.extensions

import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR


fun Date.format(pattern:String="HH:mm:ss dd.MM.yy"):String{
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))

    return dateFormat.format(this)
}

fun Date.add(value:Int, units:TimeUnits=TimeUnits.SECOND):Date{
    var time = this.time

    time += when (units){
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date=Date()): String {
    fun getTimeUnitValue(diff:Long, unit:TimeUnits):String{
        val unitDiff = diff.toDouble() /
                when (unit) {
                    TimeUnits.SECOND -> SECOND
                    TimeUnits.MINUTE -> MINUTE
                    TimeUnits.HOUR -> HOUR
                    TimeUnits.DAY -> DAY
                }

         val unitValue = unitDiff.roundToInt().toString()

        return "$unitValue "+when (unit){
            TimeUnits.SECOND ->{"секунд"+
                when (unitValue[unitValue.lastIndex]){
                    '1' -> if(unitValue.getOrNull(unitValue.lastIndex-1)=='1') "" else "у"
                    in '2'..'4' -> "ы"
                    else -> ""
                }
            }
            TimeUnits.MINUTE -> {"минут"+
                    when (unitValue[unitValue.lastIndex]){
                        '1' -> if(unitValue.getOrNull(unitValue.lastIndex-1)=='1') "" else "у"
                        in '2'..'4' -> "ы"
                        else -> ""
                    }
            }
            TimeUnits.HOUR -> {"час"+
                    when (unitValue[unitValue.lastIndex]){
                        '1' -> if(unitValue.getOrNull(unitValue.lastIndex-1)=='1') "ов" else ""
                        in '2'..'4' -> "а"
                        else -> "ов"
                    }
            }
            TimeUnits.DAY -> {"д"+
                    when (unitValue[unitValue.lastIndex]){
                        '1' -> if(unitValue.getOrNull(unitValue.lastIndex-1)=='1') "ней" else "ень"
                        in '2'..'4' -> "ня"
                        else -> "ней"
                    }
            }
        }

    }

    val timeDiff = (date.time - this.time)
    val timeDiffUnsigned = if (timeDiff>0) timeDiff else -timeDiff
    val postfix = if (timeDiff>0) " назад" else ""
    val prefix = if (timeDiff<0) "через " else ""

    return when(timeDiffUnsigned){
        in 0 until SECOND            -> "только что"
        in SECOND until 45*SECOND    -> "${prefix}несколько секунд$postfix"
        in 45*SECOND until 75*SECOND -> "${prefix}минуту$postfix"
        in 75*SECOND until 45*MINUTE -> "$prefix${getTimeUnitValue(timeDiffUnsigned, TimeUnits.MINUTE)}$postfix"
        in 45*MINUTE until 75*MINUTE -> "${prefix}час$postfix"
        in 75*MINUTE until 22*HOUR   -> "$prefix${getTimeUnitValue(timeDiffUnsigned, TimeUnits.HOUR)}$postfix"
        in 22*HOUR until 26*HOUR     -> "${prefix}день"
        in 26*HOUR until 360*DAY     -> "$prefix${getTimeUnitValue(timeDiffUnsigned, TimeUnits.DAY)}$postfix"
        else                         -> if (timeDiff>0) "более года назад" else "более чем через год"
    }
}


enum class TimeUnits{
    SECOND,
    MINUTE,
    HOUR,
    DAY
}