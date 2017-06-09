package org.nomadblacky.github.io

import better.files._
import org.nomadblacky.github.io.model.Blog

import scala.io.Source

/**
 * Created by blacky on 17/05/10.
 */
object DeployBlogs {

  lazy val usage: String = Source.fromURL(getClass.getResource("usage.txt")).getLines().mkString("\n")

  def buildBlogListMarkDown(blogs: Seq[File], currentDir: File = File(".")): String = blogs
    .filter(!_.isDirectory)
    .map { f => (f, f.lines.take(1).map(_.replaceAll("""^#+\s+""", ""))) }
    .map { case (f, tr) => s"+ [${f.name} ${tr.mkString}](${currentDir.relativize(f).toString})" }
    .mkString("\n")

  def writeIndex(blogs: Seq[File], config: CommandLineConfig): File = {
    val markdown =
      config.indexTemplate.toScala.lines
        .map { s =>
          if (s.matches("""^::articles::$"""))
            buildBlogListMarkDown(blogs)
          else
            s
        }

    config.indexFile.toScala
      .createIfNotExists()
      .clear()
      .append(markdown.mkString("\n"))
  }

  def convertMarkdownToHtml(config: CommandLineConfig): List[File] = {
    val blogs = config.blogsDir.toScala
      .listRecursively
      .filter(!_.isDirectory)
      .map(Blog.read(_))

    val pairs = blogs
      .map(b => (b, config.destDir.toScala / (b.name + ".html")))
      .toList

    config.destDir.toScala.createIfNotExists(asDirectory = true, createParents = true)
    pairs.foreach {
      case (blog, dest) =>
        dest.createIfNotExists()
          .clear()
          .write(blog.convertedText)
    }

    pairs.map(_._2)
  }

  def run(config: CommandLineConfig): Unit = {
    val markdowns = convertMarkdownToHtml(config)
    writeIndex(markdowns, config)
  }

}