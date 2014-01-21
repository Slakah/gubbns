package app.util

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.joda.time.{DateTime, DateTimeUtils}
import util.PrettyDate

class PrettyDateTest {

  @Test
  def futureOneDay() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-02")),
      equalTo("in 1 day"))
  }

  @Test
  def pastOneDay() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-07-31")),
      equalTo("1 day ago"))
  }

  @Test
  def futureThreeDays() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-04")),
      equalTo("in 3 days"))
  }

  @Test
  def pastThreeDay() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-07-29")),
      equalTo("3 days ago"))
  }

  @Test
  def futureEndDay() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T23:30")),
      equalTo("in 1 day"))
  }

  @Test
  def pastStartDay() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01T23:30").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01")),
      equalTo("1 day ago"))
  }

  @Test
  def futureOneHour() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T01:00")),
      equalTo("in 1 hour"))
  }

  @Test
  def pastOneHour() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01T05:00").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T04:00")),
      equalTo("1 hour ago"))
  }

  @Test
  def pastOneHourThirtyMinutes() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01T05:00").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T03:30")),
      equalTo("2 hours ago"))
  }

  @Test
  def pastThirtySeconds() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01T05:00").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T04:59:30")),
      equalTo("30 seconds ago"))
  }

  @Test
  def past59Min29Sec() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01T05:00").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T04:00:31")),
      equalTo("59 minutes ago"))
  }

  @Test
  def past59Min30Sec() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01T05:00").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T04:00:30")),
      equalTo("1 hour ago"))
  }

  @Test
  def pastLessThanOneSeconds() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01T05:00").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T05:00").minusMillis(1)),
      equalTo("recently"))
  }

  @Test
  def futureLessThanOneSeconds() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01T05:00").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T05:00").plusMillis(1)),
      equalTo("recently"))
  }

  @Test
  def surpassThresholdPast() {
    val nowMinusThreshold = DateTime.parse("2010-08-01")
    val now = nowMinusThreshold.plus(PrettyDate.DateThreshold).plusMillis(1)
    DateTimeUtils.setCurrentMillisFixed(now.getMillis)
    assertThat(PrettyDate.print(nowMinusThreshold), equalTo("on 01-08-2010"))
  }

  @Test
  def surpassThresholdFuture() {
    val nowPlusThreshold = DateTime.parse("2010-08-01")
    val now = nowPlusThreshold.minus(PrettyDate.DateThreshold)
    DateTimeUtils.setCurrentMillisFixed(now.getMillis)
    assertThat(PrettyDate.print(nowPlusThreshold), equalTo("on 01-08-2010"))
  }

  @Test
  def pastOneWeek() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-08").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01")),
      equalTo("1 week ago"))
  }

  @Test
  def futureOneWeek() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-09")),
      equalTo("in 1 week"))
  }

  @Test
  def pastOneWeek4Days() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-12").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01")),
      equalTo("2 weeks ago"))
  }

  @Test
  def futureOneWeek6Days() {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-14").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01")),
      equalTo("2 weeks ago"))
  }
}
