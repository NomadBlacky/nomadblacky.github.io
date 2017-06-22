package org.nomadblacky.github.io

import better.files._
import org.nomadblacky.github.io.model.Blog

import scala.io.Source

/**
 * Created by blacky on 17/05/10.
 */
object DeployBlogs {

  lazy val usage: String = Source.fromURL(getClass.getResource("usage.txt")).getLines().mkString("\n")

  def buildBlogListMarkDown(blogsAndDestFiles: Seq[(Blog, File)], currentDir: File = File(".")): String =
    blogsAndDestFiles
      .filter(!_._2.isDirectory)
      .map { case (blog, dest) => s"+ [${blog.title}](${currentDir.relativize(dest).toString})" }
      .mkString("\n")

  def writeIndex(blogsAndDestFiles: Seq[(Blog, File)], config: CommandLineConfig): File = {
    val markdown =
      config.indexTemplate.toScala.lines
        .map { s =>
          if (s.matches("""^::articles::$"""))
            buildBlogListMarkDown(blogsAndDestFiles)
          else
            s
        }

    config.indexFile.toScala
      .createIfNotExists()
      .clear()
      .append(markdown.mkString("\n"))
  }

  def convertMarkdownToHtml(config: CommandLineConfig): List[(Blog, File)] = {
    val blogs = config.blogsDir.toScala
      .listRecursively
      .filter(!_.isDirectory)
      .map(Blog.read)

    val pairs = blogs
      .map(b => (b, config.destDir.toScala / (b.fileName + ".html")))
      .toList

    config.destDir.toScala.createIfNotExists(asDirectory = true, createParents = true)
    pairs.foreach {
      case (blog, dest) =>
        dest.createIfNotExists()
          .clear()
          .write(blog.convertedText)
    }

    pairs
  }

  def run(config: CommandLineConfig): Unit = {
    val blogAndDestFile = convertMarkdownToHtml(config)
    writeIndex(blogAndDestFile, config)
  }

}