package controllers

import org.specs2.mutable._
import play.api.Logger
import play.api.test.WithBrowser

class AuthSpec extends Specification {
  "Auth" should {
    "allow login" in new WithBrowser {
      browser.goTo(routes.Auth.login.url)
      browser.fill("#email").`with`("joe.bloggs@email.com")
      browser.fill("#password").`with`("password")
      browser.submit("button")
      Logger.info(browser.pageSource())
    }
  }
}
