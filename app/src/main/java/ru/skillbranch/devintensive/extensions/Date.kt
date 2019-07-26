package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

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

        return unit.plural(unitDiff.roundToInt())

    }

    val timeDiff = (date.time - this.time)
    val timeDiffUnsigned = abs(timeDiff)
    val postfix = if (timeDiff>0) " назад" else ""
    val prefix = if (timeDiff<0) "через " else ""

    return when(timeDiffUnsigned){
        in 0 .. SECOND            -> "только что"
        in SECOND .. 45*SECOND    -> "${prefix}несколько секунд$postfix"
        in 45*SECOND .. 75*SECOND -> "${prefix}минуту$postfix"
        in 75*SECOND .. 45*MINUTE -> "$prefix${getTimeUnitValue(timeDiffUnsigned, TimeUnits.MINUTE)}$postfix"
        in 45*MINUTE .. 75*MINUTE -> "${prefix}час$postfix"
        in 75*MINUTE .. 22*HOUR   -> "$prefix${getTimeUnitValue(timeDiffUnsigned, TimeUnits.HOUR)}$postfix"
        in 22*HOUR .. 26*HOUR     -> "${prefix}день$postfix"
        in 26*HOUR .. 360*DAY     -> "$prefix${getTimeUnitValue(timeDiffUnsigned, TimeUnits.DAY)}$postfix"
        else                         -> if (timeDiff>0) "более года назад" else "более чем через год"
    }
}


enum class TimeUnits{
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(value:Int):String{
        val unitValue = value.toString()
        return "$unitValue "+when (this){
            SECOND ->{"секунд"+
                    when (unitValue[unitValue.lastIndex]){
                        '1' -> if(unitValue.getOrNull(unitValue.lastIndex-1)=='1') "" else "у"
                        in '2'..'4' -> if(unitValue.getOrNull(unitValue.lastIndex-1)=='1') "" else "ы"
                        else -> ""
                    }
            }
            MINUTE -> {"минут"+
                    when (unitValue[unitValue.lastIndex]){
                        '1' -> if(unitValue.getOrNull(unitValue.lastIndex-1)=='1') "" else "у"
                        in '2'..'4' -> if(unitValue.getOrNull(unitValue.lastIndex-1)=='1') "" else "ы"
                        else -> ""
                    }
            }
            HOUR -> {"час"+
                    when (unitValue[unitValue.lastIndex]){
                        '1' -> if(unitValue.getOrNull(unitValue.lastIndex-1)=='1') "ов" else ""
                        in '2'..'4' -> if(unitValue.getOrNull(unitValue.lastIndex-1)=='1') "ов" else "а"
                        else -> "ов"
                    }
            }
            DAY -> {"д"+
                    when (unitValue[unitValue.lastIndex]){
                        '1' -> if(unitValue.getOrNull(unitValue.lastIndex-1)=='1') "ней" else "ень"
                        in '2'..'4' -> if(unitValue.getOrNull(unitValue.lastIndex-1)=='1') "ней" else "ня"
                        else -> "ней"
                    }
            }
        }
    }
}