package org.nomadblacky.github.io.model

import org.pegdown.PegDownProcessor

sealed trait FileType {
  def convert(lines: Traversable[String]): String
}

object FileType {
  def get(extension: String): FileType = extension match {
    case ".md"   => Markdown
    case ".html" => Html
    case _       => Unknown
  }
}

object Markdown extends FileType {
  lazy val pegdown = new PegDownProcessor()

  override def convert(lines: Traversable[String]): String = {
    pegdown.markdownToHtml(lines.mkString("\n"))
  }
}

object Html extends FileType {
  override def convert(lines: Traversable[String]): String = lines.mkString("\n")
}

object Unknown extends FileType {
  override def convert(lines: Traversable[String]): String = lines.mkString("\n")
}

