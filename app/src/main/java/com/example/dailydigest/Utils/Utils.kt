package Utils


import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


class Utils {

    companion object {


        fun convertTimestampToDate(timeStamp: Long): String {
            val isMilliseconds = timeStamp > 15_000_000_000L
            val instant = if (isMilliseconds) {
                kotlinx.datetime.Instant.fromEpochMilliseconds(timeStamp)
            } else {
                kotlinx.datetime.Instant.fromEpochSeconds(timeStamp)
            }
            val timeZone = TimeZone.currentSystemDefault()
            val localDateTime = instant.toLocalDateTime(timeZone)
            val formattedDate = localDateTime.dayOfMonth.toString().padStart(2, '0')
            val formattedMonth = localDateTime.monthNumber.toString().padStart(2, '0')
            val formattedYear = localDateTime.year.toString().padStart(2, '0')
            return "$formattedYear-$formattedMonth-$formattedDate"
        }

        fun convertTimestampToLongDate(timeStamp: Long): String {
            val isMilliseconds = timeStamp > 15_000_000_000L
            val instant = if (isMilliseconds) {
                Instant.fromEpochMilliseconds(timeStamp)
            } else {
                Instant.fromEpochSeconds(timeStamp)
            }
            val timeZone = TimeZone.currentSystemDefault()
            val localDateTime = instant.toLocalDateTime(timeZone)

            val dayOfWeek = localDateTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
            val dayOfMonth = localDateTime.dayOfMonth
            val month = localDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
            val year = localDateTime.year

            val daySuffix = getDayOfMonthSuffix(dayOfMonth)

            return "$dayOfWeek $dayOfMonth$daySuffix $month $year"
        }

        fun getDayOfMonthSuffix(day: Int): String {
            return when {
                day in 11..13 -> "th"
                day % 10 == 1 -> "st"
                day % 10 == 2 -> "nd"
                day % 10 == 3 -> "rd"
                else -> "th"
            }
        }

        fun greetingsText(): String{
            val instant = Instant.fromEpochSeconds(Clock.System.now().toEpochMilliseconds())
            val timeZone = TimeZone.currentSystemDefault()
            val localDateTime = instant.toLocalDateTime(timeZone)

            return if (localDateTime.hour < 17){
                "Good Afternoon \uD83D\uDC4B"
            } else if(localDateTime.hour < 12){
               "Good Morning \uD83D\uDC4B"
            } else {
                "Good Evening \uD83D\uDC4B"
            }
        }

        fun convertTimestampToDayOfWeek(timeStamp: Long): String {
            val isMilliseconds = timeStamp > 15_000_000_000L
            val instant = if (isMilliseconds) {
                Instant.fromEpochMilliseconds(timeStamp)
            } else {
                Instant.fromEpochSeconds(timeStamp)
            }

            val timeZone = TimeZone.currentSystemDefault()
            val localDateTime = instant.toLocalDateTime(timeZone)

            return localDateTime.dayOfWeek.name
        }

        fun convertTimestampToDate(dateString: String): Long {
            val instant = Instant.parse(dateString)
            return instant.toEpochMilliseconds()
        }


        fun convertMillisecondsToTimeTaken(milliseconds: Long): String {
            val hours = (milliseconds / (1000 * 60 * 60)).toString().padStart(2, '0')
            val minutes =
                ((milliseconds % (1000 * 60 * 60)) / (1000 * 60)).toString().padStart(2, '0')
            val seconds = ((milliseconds % (1000 * 60)) / 1000).toString().padStart(2, '0')
            return "$hours:$minutes:$seconds"
        }

        fun calculatePercentageOfTime(milliseconds: Long): Float {
            val minutes = (milliseconds / (1000 * 60)).toFloat()
            val percentage = (minutes / 60.0) * 100
            return percentage.toFloat()
        }



        fun combineDateTimeToTimestamp(dateTimestamp: Long, selectedTime: LocalTime): Long {
            val dateInstant: Instant = Instant.fromEpochMilliseconds(dateTimestamp)
            val localDateTime: LocalDateTime =  dateInstant.toLocalDateTime(TimeZone.currentSystemDefault())
            val formattedHour = selectedTime.hour.toString().padStart(2, '0')
            val formattedMinute = selectedTime.minute.toString().padStart(2, '0')
            val formattedSecond = selectedTime.second.toString().padStart(2, '0')
            val combinedDateTimeString = "${localDateTime.date}T$formattedHour:$formattedMinute:$formattedSecond"
            val combinedLocalDateTime: LocalDateTime = LocalDateTime.parse(combinedDateTimeString)
            val combinedInstant: Instant =    combinedLocalDateTime.toInstant(TimeZone.currentSystemDefault())
            return combinedInstant.toEpochMilliseconds()
        }

        fun convertTimestampToTime(timeStamp: Long): String {
            val isMilliseconds = timeStamp > 15_000_000_000L
            val instant = if (isMilliseconds) {
                Instant.fromEpochMilliseconds(timeStamp)
            } else {
                Instant.fromEpochSeconds(timeStamp)
            }
            val timeZone = TimeZone.currentSystemDefault()
            val localDateTime = instant.toLocalDateTime(timeZone)

            val formattedHour = localDateTime.hour.toString().padStart(2, '0')
            val formattedMinute = localDateTime.minute.toString().padStart(2, '0')
            val formattedSeconds = localDateTime.second.toString().padStart(2, '0')
            return "$formattedHour:$formattedMinute:$formattedSeconds"
        }

        fun convertStringDateToEpochMilliseconds(dateTime: String): Long {
            val instant = Instant.parse(dateTime)
            val timeZone = TimeZone.currentSystemDefault()
            val localDateTime = instant.toLocalDateTime(timeZone)
            return localDateTime.toInstant(timeZone).toEpochMilliseconds()
        }


        fun checkIfTimestampIsSameDay(timeStamp: Long): Boolean {
            val currentInstant = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
            val selectedInstant = Instant.fromEpochMilliseconds(timeStamp)

            val currentTime = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
            val selectedTime = selectedInstant.toLocalDateTime(TimeZone.currentSystemDefault())

            return currentTime.date == selectedTime.date
        }



         fun decodeExceptionMessage(e: Exception): String{
            e.printStackTrace()
            if(e.message?.lowercase()?.contains("failed to connect") == true){
                return "check your internet connection and try again"
            }
            return  "Something went wrong. Try again"
        }

    }
}