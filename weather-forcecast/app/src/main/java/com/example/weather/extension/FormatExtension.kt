package com.example.weather.extension

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


fun Date.stringFormat() = DateTimeFormatter.ofPattern(DISPLAY_DATE_FORMAT).format(
    ZonedDateTime.from(this.toInstant().atZone(ZoneId.systemDefault()))
)

const val DISPLAY_DATE_FORMAT = "EEE dd MMM yyyy"