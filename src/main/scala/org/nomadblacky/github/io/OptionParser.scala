package org.nomadblacky.github.io

import java.io.{ File ⇒ JFile }

import scala.io.Source

/**
 * Created by blacky on 17/06/02.
 */
object OptionParser {
  val parser = new scopt.OptionParser[CommandLineConfig]("nomadblacky.github.io") {
    opt[JFile]('o', "dest-dir").action((f, c) ⇒ c.copy(destDir = f))
    opt[JFile]('t', "index-template").action((f, c) ⇒ c.copy(indexTemplate = f))
    opt[JFile]('b', "blogs-dir").action((f, c) ⇒ c.copy(blogsDir = f))
    help("help").text(Source.fromURL(getClass.getResource("usage.txt")).getLines().mkString("\n"))
  }
}

case class CommandLineConfig(
  destDir: JFile = new JFile("_build"),
  indexTemplate: JFile = new JFile("templates/index.md"),
  blogsDir: JFile = new JFile("blogs"),
  indexFile: JFile = new JFile("index.md")
)
