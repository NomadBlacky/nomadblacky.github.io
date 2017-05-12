package org.nomadblacky.github.io

import java.io.FileNotFoundException

import better.files._

import scala.io.Source
import scala.util.Try

/**
 * Created by blacky on 17/05/10.
 */
object DeployBlogs {

  def getFile(path: String, isDirectory: Boolean): Try[File] = Try {
    val f = File(path)
    if (!f.exists) {
      throw new FileNotFoundException(s"${path} is not found.")
    }
    if (f.isDirectory ^ isDirectory) {
      val msg = if (f.isDirectory) "directory" else "file"
      throw new IllegalStateException(s"${path} is a ${msg}")
    }
    f
  }

  def getUsage(): String = {
    Source.fromURL(getClass.getResource("usage.txt")).getLines().mkString("\n")
  }

  def buildBlogList(baseDir: File): String = {
    "hoge"
  }

  def run(args: Array[String]): Unit = {
    val templateFile = getFile(args(0), false).get
    val blogsDir = getFile(args(1), true).get

    val markdown =
      templateFile.lines
        .map { s ⇒
          if (s.matches("""^::articles::$"""))
            buildBlogList(blogsDir)
          else
            s
        }

    File("index.md")
      .createIfNotExists()
      .clear()
      .append(markdown.mkString("\n"))
  }

  def main(args: Array[String]): Unit = {
    if (args.size == 0) println(getUsage())
    else run(args)
  }
}