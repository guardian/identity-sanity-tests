package com.gu.util

import org.slf4j.LoggerFactory

object TargetHost {
  def hosts(): List[String] = targetHosts(stage())

  class TargetHostException(msg: String) extends Exception(msg: String)

  private[this] val targetHosts = Map(
    "CODE" -> List("https://idapi.code.dev-theguardian.com",
      "https://id.code.dev-guardianapis.com"),
    "PROD" -> List("https://idapi.theguardian.com",
      "https://id.guardianapis.com")
  )

  private[this] val stageEnvVarName = "ID_API_TARGET_STAGE"

  private[this] def stage(): String = {
    sys.env.get(stageEnvVarName) match {
      case Some(v) =>
        logger.info("Target stage: " + v)
        if ((v == "PROD") || (v == "CODE")) v
        else {
          val errMsg = "Environmental variable " + stageEnvVarName +
            " has wrong value. It should be either CODE or PROD."
          logger.error(errMsg)
          throw new TargetHostException(errMsg)
        }
      case None =>
        throw new TargetHostException(
          "Environmental variable " + stageEnvVarName + " not set.")
    }
  }

  private[this] val logger = LoggerFactory.getLogger(this.getClass)
}
