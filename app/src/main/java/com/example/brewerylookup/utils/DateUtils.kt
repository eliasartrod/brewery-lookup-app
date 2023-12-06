package com.example.inventory.utils

import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private const val dayFormat = "dd"
    private const val monthFormat = "MMM"
    private const val mediumFormat = "EEEE, MM/dd"
    private const val mediumNumericFormat = "MM/dd/YYY"
    private const val fullNumericFormat = "MM/dd/YYY hh:mm aa"
    fun getDay(date: Date?): String {
        return formatDate(dayFormat, date)
    }

    fun getMonth(date: Date?): String {
        return formatDate(monthFormat, date)
    }

    /**
     * Uses format EEEE, MM/dd
     *
     * @param date
     * @return
     */
    fun getMediumDateFormat(date: Date?): String {
        return formatDate(mediumFormat, date)
    }

    /**
     * Uses format MM/dd/YYY
     *
     * @param date
     * @return
     */
    fun getMediumNumericDateFormat(date: Date?): String {
        return formatDate(mediumNumericFormat, date)
    }

    fun getFullNumericFormat(date: Date?): String {
        return formatDate(fullNumericFormat, date)
    }

    fun formatDate(stringFormat: String?, date: Date?): String {
        return if (date != null) {
            val format = SimpleDateFormat(stringFormat, Locale.getDefault())
            format.format(date)
        } else {
            "Invalid Timestamp"
        }
    }

    fun isTodayInRange(from: Date?, to: Date?): Boolean {
        val compareFrom = DateTime(from)
        return if (to != null) {
            val compareTo = DateTime(to)
            compareFrom.isBeforeNow() && compareTo.isAfterNow()
        } else {
            compareFrom.isBeforeNow()
        }
    }

    fun isThisWeekInRange(from: Date?, to: Date?): Boolean {
        val today = DateTime()
        val compareFrom = DateTime(from)
        return if (to != null) {
            val compareTo = DateTime(to)
            compareFrom.getWeekOfWeekyear() <= today.getWeekOfWeekyear() &&
                    compareTo.getWeekOfWeekyear() >= today.getWeekOfWeekyear()
        } else {
            compareFrom.getWeekOfWeekyear() <= today.getWeekOfWeekyear()
        }
    }

    fun isNextWeekInRange(from: Date?, to: Date?): Boolean {
        val nextWeek = DateTime()
        val compareFrom = DateTime(from)
        return if (to != null) {
            val compareTo = DateTime(to)
            compareFrom.getWeekOfWeekyear() <= nextWeek.getWeekOfWeekyear() + 1 &&
                    compareTo.getWeekOfWeekyear() >= nextWeek.getWeekOfWeekyear() + 1
        } else {
            compareFrom.getWeekOfWeekyear() <= nextWeek.getWeekOfWeekyear() + 1
        }
    }

    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        require(!(date1 == null || date2 == null)) { "The dates must not be null" }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return cal1[Calendar.ERA] == cal2[Calendar.ERA] && cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
    }

    fun isToday(date: Date?): Boolean {
        return isSameDay(date, Calendar.getInstance().time)
    }

    /**
     * Checks if a particular date is within today and daysInFuture days ahead
     *
     * @param date
     * @param daysInFuture
     * @return true if date is within range, false otherwise
     */
    fun isInNextDays(date: Date?, daysInFuture: Int): Boolean {
        // daysInFuture days ahead
        val now = DateTime()
        val future: DateTime = now.plusDays(daysInFuture)
        val dateTime = DateTime(date)
        return dateTime.isBefore(future)
    }

    fun cvtToGmt(date: Date): Date {
        val tz = TimeZone.getDefault()
        var ret = Date(date.time - tz.rawOffset)

        // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
        if (tz.inDaylightTime(ret)) {
            val dstDate = Date(ret.time - tz.dstSavings)

            // check to make sure we have not crossed back into standard time
            // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
            if (tz.inDaylightTime(dstDate)) {
                ret = dstDate
            }
        }
        return ret
    }
}