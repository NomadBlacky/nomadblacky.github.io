package org.nomadblacky.github.io.model

import java.util.logging.Logger

import scala.util.matching.Regex

/**
 * Created by blacky on 17/05/24.
 */
case class InlineConfig(key: String, value: String)

object InlineConfig {

  lazy val keyValueRegex: Regex = new Regex("""^\s*(\w+)\s*:\s*(.+)\s*$""", "key", "value")

  lazy val logger: Logger = Logger.getLogger(getClass.getName)

  def read(sourceLines: Seq[String], throwIfInvalid: Boolean = true): Map[String, InlineConfig] = {
    val configs = getStatement(sourceLines).filter(!_.trim.isEmpty).map(getConfig)
    val invalids = configs.filter(_.isLeft).map(_.left.get)
    if (invalids.nonEmpty && throwIfInvalid) {
      throw new IllegalStateException(invalids.mkString("\n"))
    }
    configs.filter(_.isRight).map(_.right.get).map(cf => (cf.key, cf)).toMap
  }

  def getConfig(line: String): Either[String, InlineConfig] = keyValueRegex
    .findFirstMatchIn(line)
    .map(m ⇒ InlineConfig(m.group("key"), m.group("value")))
    .toRight(s"Invalid input $line")

  def getStatement(lines: Seq[String]): Seq[String] = lines
    .dropWhile(!_.matches("""^===config===$"""))
    .takeWhile(!_.matches("""^===end\s+config===$"""))
    .tail
}

trait Reader[A] {
  def read(a: A): List[InlineConfig]
}
