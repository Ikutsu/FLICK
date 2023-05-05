package com.ikuzMirel.flick.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}