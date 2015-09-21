package com.gu.util

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients

import scala.io.Source

/**
 * Utility Http client
 */
object Http {

  def GET(url: String): String =
  {
    val client = HttpClients.createDefault()
    val request = new HttpGet(url)
    val response = client.execute(request)

    // return response as string
    Source.fromInputStream(
      response.getEntity().getContent).getLines().mkString("")
  }
}
