package util

import org.specs2.mutable.Specification

class SlugifySpec extends Specification {
  "Slugify" should {
    "replace spaces with -" in {
      Slugify.slugify("lorem ipsum dolor sit amet") must equalTo("lorem-ipsum-dolor-sit-amet")
    }

    "upper case letters should be changed to lower case" in {
      Slugify.slugify("LorEmiPsuM") must equalTo("loremipsum")
    }

    "percent character % should be encoded to %25" in {
      Slugify.slugify("Increased by 5%") must equalTo("increased-by-5%25")
    }

    "forward slash / should be encoded to %2F" in {
      Slugify.slugify("10/02/2013 is a good date") must equalTo("10%2F02%2F2013-is-a-good-date")
    }
  }
}
