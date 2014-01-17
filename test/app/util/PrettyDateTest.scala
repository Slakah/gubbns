package app.util

import org.junit.{Before, Test}
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.joda.time.{DateTime, DateTimeUtils}
import util.PrettyDate

class PrettyDateTest {

  @Test
  def futureDate {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-02")),
      equalTo("02-08-2010"));
  }

  @Test
  def pastDate {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-07-31T23:59")),
      equalTo("31-07-2010"));
  }

  @Test
  def futureEndDay {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T23:59")),
      equalTo("23 hours in the future"));
  }

  @Test
  def pastStartDay {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01T23:59").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01")),
      equalTo("23 hours ago"));
  }

  @Test
  def futureOneHour {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T01:00")),
      equalTo("1 hour in the future"));
  }

  @Test
  def pastOneHour {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01T05:00").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T04:00")),
      equalTo("1 hour ago"));
  }

  @Test
  def pastOneHourThirtyMin {
    DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2010-08-01T05:00").getMillis)
    assertThat(PrettyDate.print(DateTime.parse("2010-08-01T03:30")),
      equalTo("1 hour ago"));
  }
}
