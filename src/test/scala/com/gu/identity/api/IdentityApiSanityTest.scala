package com.gu.identity.api

import com.gu.util.TargetHost
import com.gu.util.Http

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.slf4j.LoggerFactory

/**
 * Sanity test of public Identity API on CODE
 */
class IdentityApiSanityTest extends FlatSpec with Matchers {

  private[this] val logger = LoggerFactory.getLogger(this.getClass)

  private[this] val idApiHost = TargetHost.idApiHost()

  behavior of "Public Identity API on CODE"

  it should "return the user corresponding to given ID" in {
    val userId = "10000001"
    val response = Http.GET(s"$idApiHost/user/$userId")
    response should include(userId)
  }

  it should "not expose private user information" in {
    val userId = "10000001"
    val response = Http.GET(s"$idApiHost/user/$userId")
    response should(
      not include "primaryEmailAddress"
        and not include "privateFields")
  }
}
