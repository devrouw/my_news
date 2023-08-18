package com.lollipop.mynews.helper

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateFormatLocale {
    val locale = Locale("in", "ID")
    private const val ASIA_JAKARTA = "Asia/Jakarta"
    val timeWithoutMinutes = SimpleDateFormat("HH:mm", locale)

    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)
    val dateWithMonth = SimpleDateFormat("dd MMMM yyyy", locale)

    private val localeDateTime = SimpleDateFormat("EEEE, d MMMM y", locale)

    /**
     * Get this date time with eg format 2020-12-12 12:12:12
     *
     *  Example input : Asia/Jakarta (default param)
     *
     *  Example output : 2017-11-09 00:00:00
     */
    fun getDateTimeNow(timeZone: String = ASIA_JAKARTA): String {

        return try {
            timestamp.timeZone = TimeZone.getTimeZone(timeZone)
            timestamp.format(Date())
        } catch (ex: Exception) {
            ""
        }
    }

    /**
     *  Create Time Different Date format
     *
     *  example output: 1 tahun lalu
     *
     *  example input: params: 2021-02-08T13:00:00
     *
     *  note* must replace space to T
     *
     *  example use: convertToTimeDiff("2021-02-08T13:00:00")
     */
    @SuppressLint("SimpleDateFormat")
    fun covertToTimeDiff(dataDate: String): String? {
        var result: String? = null
        val suffix = "ago"
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val pasTime = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime?.time!!
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                result = "$second second $suffix"
            } else if (minute < 60) {
                result = "$minute minute $suffix"
            } else if (hour < 24) {
                result = "$hour hour $suffix"
            } else if (day >= 7) {
                result = if (day > 360) {
                    (day / 360).toString() + " year " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " month " + suffix
                } else {
                    (day / 7).toString() + " week " + suffix
                }
            } else if (day < 7) {
                result = "$day day $suffix"
            }
        } catch (e: ParseException) {
            result = "null"
        }
        return result
    }

    /**
     *  Create Time Different Date format but limited to hours
     *
     *  example output: 1 jam yang lalu
     *
     *  example input: params: 2021-02-08 13:00:00
     *
     *
     *  example use: timeDiffOrFullMonth("2021-02-08 13:00:00")
     */
    fun timeDiffOrFullMonth(dataDate: String): String {
        var result: String? = null
        val suffix = "ago"
        try {
            val pasTime = timestamp.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime?.time!!
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                result = "$second second $suffix"
            } else if (minute < 60) {
                result = "$minute minute $suffix"
            } else if (hour < 24) {
                result = "$hour hour $suffix"
            } else if (day >= 1) {
                result = dateWithMonth.format(pasTime) + ", " + timeWithoutMinutes.format(pasTime)
            }
        } catch (e: ParseException) {
            result = "null"
        }
        return "last active $result"
    }
}