package util

import play.utils.UriEncoding

object Slugify {
  /**
   * Convert text to url freindly version http://en.wikipedia.org/wiki/Clean_URL
   * @param raw
   * @return
   */
  def slugify(raw: String) = UriEncoding.encodePathSegment(raw.replaceAll(" ", "-").toLowerCase, "utf-8")
}
