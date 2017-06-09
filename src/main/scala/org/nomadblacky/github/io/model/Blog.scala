package org.nomadblacky.github.io.model

import better.files.File
import org.pegdown.PegDownProcessor

/**
 * Created by blacky on 17/06/03.
 */
case class Blog(file: File, fType: FileType, config: Map[String, InlineConfig]) {
  lazy val name: String = file.nameWithoutExtension
  lazy val convertedText: String = fType.convert(file.lines)
}

object Blog {
  def read(file: File): Blog = {
    val config = InlineConfig.read(file.lines.toSeq)
    Blog(file, FileType.get(file.extension().getOrElse("")), config)
  }
}

sealed trait FileType {
  def convert(lines: Traversable[String]): String
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

object FileType {
  def get(extension: String): FileType = extension match {
    case ".md"   ⇒ Markdown
    case ".html" ⇒ Html
    case _       ⇒ Unknown
  }
}