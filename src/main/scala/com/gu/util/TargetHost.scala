package com.gu.util

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import scala.util.Try
import scala.util.Success
import scala.util.Failure

object TargetHost {

  private[this] val logger = LoggerFactory.getLogger(this.getClass)

  case class TargetHostException(msg: String) extends Exception(msg: String)

  /**
   * Reads from configuration file or environmental variable the Identity API
   * host against which the tests should run. The host can be in LOCAL, CODE or
   * PROD.
   *
   * Environmental variable IDENTITY_API_HOST has priority over
   * identity.api.host property in application.conf file.
   *
   * TeamCity uses IDENTITY_API_HOST to target the tests.
   *
   * @return Identity API host
   */
  def idApiHost(): Try[String] =
    readIdApiHostFromEnvVar orElse readIdApiHostFromConfigFile match {
      case Failure(e) => Failure(TargetHostException(
        "Target host not set. Specify target host either " +
          "in environmental variable or application.conf"))
      case Success(targetHost) => Success(targetHost)
    }


  /* Reads IDENTITY_API_HOST environmental variable */
  private[this] def readIdApiHostFromEnvVar(): Try[String] = {
    sys.env.get("IDENTITY_API_HOST") match {
      case Some(v) => {
        logger.info("Target host = " + v)
        logger.info("Target host specified using environmental variable. ")
        Success(v)
      }
      case None => {
        Failure(TargetHostException(
          "Environmental variable IDENTITY_API_HOST not set."))
      }
    }
  }

  /* Reads identity.api.host property from application.conf */
  private[this] def readIdApiHostFromConfigFile(): Try[String] = {
    val conf = ConfigFactory.load()

    Try(conf.getString("identity.api.host")) match {
      case Success(v) => {
        logger.info("Target host = " + v)
        logger.info("Target host specified using application.conf. ")
        Success(v)
      }
      case Failure(e) => {
        Failure(TargetHostException(
          "Property identity.api.host not set in application.conf."))
      }
    }
  }

}
