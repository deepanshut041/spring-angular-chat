package com.squrlabs.sca.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

object DateTimeUtil {

    fun getDateFromString(str: String?): Date {
        val parsedStr: String = str?.let {
            if (it != "")  it else "2000-01-01T00:00:00.000+00:00"
        }?: run{
            "2000-01-01T00:00:00.000+00:00"
        }
        return try {
            Date.from(ZonedDateTime.parse( parsedStr, DateTimeFormatter.ISO_DATE_TIME).toInstant())
        } catch (ex: DateTimeParseException){
            throw BadRequestException("Unable to parse date provided")
        }
    }
}