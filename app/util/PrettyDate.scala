package util

import org.joda.time._
import org.joda.time.format.{DateTimeFormat, PeriodFormatterBuilder, PeriodFormatter}

object PrettyDate {
  private val dashDateFormat = DateTimeFormat.forPattern("dd-MM-yyyy")

  def print(date: DateTime) = {
    val isToday = LocalDate.now.isEqual(date.toLocalDate)
    isToday match {
      // Print 
      case true => printRelativeTime(date)
      // Print date as dd-MM-yyyy format
      case false => dashDateFormat.print(date)
    }
  }

  /**
   * Format the relative time from the present
   * < 1s -> "recently"
   * 2h 30m in the past -> "2 hours ago"
   * 32m in the future -> "32 minutes in the future"
   * etc.
   * @param date
   * @return
   */
  private def printRelativeTime(date: DateTime) = {
    val isDateInPast = date.isBeforeNow

    val timeFromNow = isDateInPast match {
      case true => new Duration(date, DateTime.now)
      case false => new Duration(DateTime.now, date)
    }
    val durationFromNow = timeFromNow.toDuration
    if (durationFromNow.isShorterThan(Seconds.ONE.toStandardDuration)) {
      "recently"
    } else {
      val discreteDuration = significantTimeUnit(durationFromNow)
      val durationWithUnits = printDurationWithUnits(discreteDuration)
      isDateInPast match {
         case true => f"$durationWithUnits%s ago"
         // Probably not needed
         case false => f"$durationWithUnits%s in the future"
      }
    }
  }

  /**
   * Return the significant time unit, i.e.
   * 2h 15m 30s -> 2h
   * 15m 50s -> 15m
   * 26s -> 26s
   * @param duration
   * @return
   */
  private def significantTimeUnit(duration: Duration): ReadablePeriod = {
    if (duration.getStandardHours > 0) duration.toStandardHours
    else if (duration.getStandardMinutes > 0) duration.toStandardMinutes
    else duration.toStandardSeconds
  }

  private def printDurationWithUnits(period: ReadablePeriod) = {
    val fmt: PeriodFormatter = new PeriodFormatterBuilder()
      .appendHours.appendSuffix(" hour", " hours")
      .appendMinutes.appendSuffix(" minute", " minutes")
      .appendSeconds.appendSuffix(" second", " seconds")
      .printZeroNever
      .toFormatter
    fmt.print(period)
  }
}
