package util

import org.joda.time._
import org.joda.time.format.{DateTimeFormat, PeriodFormatterBuilder, PeriodFormatter}

object PrettyDate {
  private val dateFormat = DateTimeFormat.forPattern("MMMM d, yyyy")
  
  // The threshold where absolute dates will be used instead of a relative time
  val DateThreshold = Weeks.weeks(2)

  /**
   * When the difference between the supplied date and now is greater
   * than [[util.PrettyDate.DateThreshold]] apply the date format
   * dd-MM-yyyy to the supplied date.
   *
   * When less than the threshold round to the significant time field
   * as per [[util.PrettyDate.printRelativeTime()]], unless less than
   * one second where "recently" will be used
   */
  def print(date: DateTime) = {
    val now = DateTime.now

    val isDateInPast = date.isBefore(now)
    val dateFromNow = isDateInPast match {
      case true => new Interval(date, now)
      case false => new Interval(now, date)
    }
    val thresholdInterval = new Interval(now.minus(DateThreshold), now.plus(DateThreshold))
    val withinThreshold = thresholdInterval.contains(date)
    
    if (withinThreshold) {
      if (dateFromNow.toDuration.isShorterThan(Seconds.ONE.toStandardDuration)) {
        "recently"
      } else {
        printRelativeTime(dateFromNow.toPeriod, isDateInPast)
      }
    } else {
      val formattedDate = dateFormat.print(date)
      f"on $formattedDate"
    }
  }

  /**
   * Format the relative time from the present
   * < 1s -> "recently"
   * 2h 30m in the past -> "2 hours ago"
   * 32m in the future -> "in 32 minutes"
   * etc.
   */
  private def printRelativeTime(period: Period, isDateInPast: Boolean) = {
    val discreteDuration = roundToSignificantField(period.toPeriod)
    val relativeTime = printDurationWithUnits(discreteDuration)
    if  (isDateInPast) {
      f"$relativeTime ago"
    } else {
      f"in $relativeTime"
    }
  }
  

  /**
   * Return the duration rounded to the nearest significant time field, i.e.
   * 3d 11h -> 3d
   * 2d 12h -> 3d
   * 2h 15m 30s -> 2h
   * 2h 30m -> 3h
   * 15m 29s -> 15m
   * 26s -> 26s
   * 56s -> 56s
   *
   * Corner case:
   * 23h 30m -> 1d
   * 23h 29m -> 23h
   */
  private def roundToSignificantField(nonNormalisedPeriod: Period) = {
    val period = nonNormalisedPeriod.normalizedStandard
    if (roundWeeks(period) > 0) Weeks.weeks(roundWeeks(period))
    else if (roundDays(period) > 0) Days.days(roundDays(period))
    else if (roundHours(period) > 0) Hours.hours(roundHours(period))
    else if (roundMinutes(period) > 0) Minutes.minutes(roundMinutes(period))
    else period.toStandardSeconds
  }

  private def roundWeeks(period: Period) =
    roundTimeField(period.getWeeks, roundDays(period),
      Weeks.ONE.toStandardDays.getDays)

  private def roundDays(period: Period) =
    roundTimeField(period.getDays, roundHours(period),
      Days.ONE.toStandardHours.getHours)

  private def roundHours(period: Period) =
    roundTimeField(period.getHours, roundMinutes(period),
      Hours.ONE.toStandardMinutes.getMinutes)

  private def roundMinutes(period: Period) =
    roundTimeField(period.getMinutes, period.getSeconds,
      Minutes.ONE.toStandardSeconds.getSeconds)


  /**
   * An additional field of time will be added to field when
   * fractionalField / conversionFactor > 0.5
   * With the additional rule that if field is 0 a fractional field can only
   * increment field when the partial field is a full field.
   * This stops 30m from rounding to 1h
   * @param field full field of time
   * @param fractionalField partial field of time
   * @param conversionFactor The factor required to convert fractionalField into field
   * @return
   */
  private def roundTimeField(field: Int, fractionalField: Int, conversionFactor: Int) = {
    if (field == 0) {
      if (fractionalField == conversionFactor) {
        // Could use math.round, but this is equivalent because fractionalField == conversionFactor
        field + 1
      } else {
        0
      }
    } else math.round(field + (fractionalField.toDouble / conversionFactor)).toInt
  }

  private def printDurationWithUnits(period: ReadablePeriod) = {
    lazy val fmt: PeriodFormatter = new PeriodFormatterBuilder()
      .appendWeeks.appendSuffix(" week", " weeks")
      .appendDays.appendSuffix(" day", " days")
      .appendHours.appendSuffix(" hour", " hours")
      .appendMinutes.appendSuffix(" minute", " minutes")
      .appendSeconds.appendSuffix(" second", " seconds")
      .printZeroNever
      .toFormatter
    fmt.print(period)
  }
}
