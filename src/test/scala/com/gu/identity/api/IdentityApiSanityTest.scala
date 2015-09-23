package com.gu.identity.api

import com.gu.util.TargetHost
import com.gu.util.Http

import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.LoggerFactory

/**
 * Sanity tests of public Identity API targeting hosts living in different stages
 * of both Web and Mobile Apps consumers of the API.
 */
class IdentityApiSanityTest extends IdentityApiBehaviors {

  private[this] val logger = LoggerFactory.getLogger(this.getClass)


  behavior of "Public Identity API for Web Apps consumers"

  private[this] val idApiHost = TargetHost.webIdApiHost()

  it should behave like itReturnsRequestedUser(idApiHost)
  it should behave like itProtectsPrivateInformation(idApiHost)


  behavior of "Public Identity API for Mobile Apps consumers"

  private[this] val mobileIdApiHost = TargetHost.mobileIdApiHost()
  
  it should behave like itReturnsRequestedUser(mobileIdApiHost)
  it should behave like itProtectsPrivateInformation(mobileIdApiHost)
}

/**
 * Same tests are executed against both Web and Mobile Apps consumers.
 *
 * They are parametrised only by the host domain name.
 */
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
