package org.nomadblacky.github.io.model

import better.files.File

/**
 * Created by blacky on 17/06/03.
 */
case class Blog(file: File, fType: FileType, config: Map[String, InlineConfig]) {
  lazy val fileName: String = file.nameWithoutExtension
  lazy val convertedText: String = fType.convert(file.lines)

  lazy val title: String = config.get("title").map(_.value).getOrElse("untitled")
}

object Blog {
  def read(file: File): Blog = {
    val config = InlineConfig.read(file.lines.toSeq)
    Blog(file, FileType.get(file.extension().getOrElse("")), config)
  }
}

