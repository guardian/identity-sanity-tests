package com.gu.util

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import scala.util.{Try, Success, Failure}

/**
 * Utility class for determining the target host against which the tests should
 * run.
 *
 * The hosts is determined using
 *   - environmental variable to specify either PROD or CODE stage
 *   - application.conf file to provide domains for each stage and each API consumer
 *
 * We differentiate between the following consumer of the API because they use different
 * domains:
 *   - Web apps (Frontend, Subscription, Membership)
 *   - Mobile apps
 */
object TargetHost {

  /**
   * Returns Identity API host intended for Web Apps consumers such as Frontend, Membership,
   * and Subscription.
   *
   * @return Identity API host for Web consumers
   * @throws TargetHostException if the host is not specified
   */
  def webIdApiHost(): String = {
    logger.info("Determining Identity API host for Web consumers...")
    idApiHostImpl("identity.api.web.")
  }

  /**
   * Returns Identity API host intended for Mobile Apps consumer.
   *
   * @return Identity API host for Mobile Apps consumer
   * @throws TargetHostException if the host is not specified
   */
  def mobileIdApiHost(): String = {
    logger.info("Determining Identity API host for Mobile consumers...")
    idApiHostImpl("identity.api.mobile.")
  }

  /**
   * Target host could not be determined.
   */
  class TargetHostException(msg: String) extends Exception(msg: String)

  private[this] val logger = LoggerFactory.getLogger(this.getClass)

  private[this] val conf = ConfigFactory.load()

  /* The name of the environmental variable specifying the target stage */
  private[this] val stageEnvVarName = "ID_API_TARGET_STAGE"

  /* Reads target stage (CODE, PROD) from environmental variable stageEnvVarName. */
  private[this] def readTargetStageFromEnvVar(): Try[String] = {

    sys.env.get(stageEnvVarName) match {
      case Some(v) =>
        logger.info("Target stage: " + v)
        if ((v == "PROD") || (v == "CODE"))
        {
          Success(v)
        }
        else
        {
          val errMsg = "Environmental variable " + stageEnvVarName +
            " has wrong value. It should be either CODE or PROD."
          logger.error(errMsg)
          Failure(new TargetHostException(errMsg))
        }
      case None =>
        Failure(new TargetHostException(
          "Environmental variable " + stageEnvVarName + " not set."))
    }
  }

  /* Read a property from the config file */
  private[this] def readPropertyFromConfigFile(property: String) : String = {
    Try(conf.getString(property)) match {
      case Success(v) =>
        v
      case Failure(e) =>
        throw new TargetHostException(property + " not set in application.conf.")
    }
  }

  /* Helper method to avoid code duplication */
  private[this] def idApiHostImpl(hostPropertyPrefix: String): String = {
    val stage = readTargetStageFromEnvVar() match {
      case Success(v) => v
      case Failure(e) => throw e
    }

    val host = readPropertyFromConfigFile(hostPropertyPrefix + stage.toLowerCase)
    logger.info("Target host: " + host)
    host
  }
}
