package org.nomadblacky.github.io.model

import scala.util.Try

/**
 * Created by blacky on 17/05/24.
 */
class InlineConfig(val key: String, val value: String)

object InlineConfig {

  def read(sourceLines: Seq[String]): Try[List[InlineConfig]] = {
    ???
  }

  def getStatement(lines: Seq[String]): Seq[String] = lines
    .dropWhile(!_.matches("""^===config===$"""))
    .takeWhile(!_.matches("""^===end\s+config===$"""))
    .tail
}

trait Reader[A] {
  def read(a: A): List[InlineConfig]
}
