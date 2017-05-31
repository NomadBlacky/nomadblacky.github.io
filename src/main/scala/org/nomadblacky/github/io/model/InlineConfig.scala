package org.nomadblacky.github.io.model

import scala.util.Try
import scala.util.matching.Regex

/**
 * Created by blacky on 17/05/24.
 */
case class InlineConfig(val key: String, val value: String)

object InlineConfig {

  lazy val keyValueRegex = new Regex("""^\s*(\w+)\s*:\s*(.+)\s*$""", "key", "value")

  def read(sourceLines: Seq[String]): Try[Map[String, InlineConfig]] = Try {
    getStatement(sourceLines)
      .map(getConfig)
      .map(c ⇒ (c.key, c))
      .toMap
  }

  def getConfig(line: String): InlineConfig = keyValueRegex
    .findFirstMatchIn(line)
    .map(m ⇒ InlineConfig(m.group("key"), m.group("value")))
    .getOrElse(throw new IllegalStateException(s"Invalid input: ${line}"))

  def getStatement(lines: Seq[String]): Seq[String] = lines
    .dropWhile(!_.matches("""^===config===$"""))
    .takeWhile(!_.matches("""^===end\s+config===$"""))
    .tail
}

trait Reader[A] {
  def read(a: A): List[InlineConfig]
}
