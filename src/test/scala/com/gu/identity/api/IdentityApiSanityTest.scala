package com.gu.identity.api

import com.gu.util.{TargetHost, Http}
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.LoggerFactory

class IdentityApiSanityTest extends IdentityApiBehaviors {
  for (host <- TargetHost.hosts()) {
    behavior of "Public Identity API on " + host
    it should behave like itReturnsRequestedUser(host)
    it should behave like itProtectsPrivateInformation(host)
  }

  private[this] val logger = LoggerFactory.getLogger(this.getClass)
}

class IdentityApiBehaviors extends FlatSpec with Matchers {
  def itReturnsRequestedUser(idApiHost: String) {
    it should "return the user corresponding to given ID" in {
      val userId = "10000001"
      val response = Http.GET(s"$idApiHost/user/$userId")
      response should include(userId)
    }
  }
  def itProtectsPrivateInformation(idApiHost: String) = {
    it should "not expose private user information" in {
      val userId = "10000001"
      val response = Http.GET(s"$idApiHost/user/$userId")
      response should (
        not include "primaryEmailAddress"
          and not include "privateFields")
    }
  }
}
