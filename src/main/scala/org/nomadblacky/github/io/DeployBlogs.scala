package org.nomadblacky.github.io

import java.io.FileNotFoundException

import better.files._
import org.pegdown.PegDownProcessor

import scala.io.Source
import scala.util.Try

/**
 *  Created by blacky on 17/05/10.
 */
object DeployBlogs {

  def getFile(pathFunc: ⇒ String, isDirectory: Boolean): Try[File] = Try {
    val f = File(pathFunc)
    if (!f.exists) {
      throw new FileNotFoundException(s"${f.path} is not found.")
    }
    if (f.isDirectory ^ isDirectory) {
      val msg = if (f.isDirectory) "directory" else "file"
      throw new IllegalStateException(s"${f.path} is a $msg")
    }
    f
  }

  lazy val usage: String = Source.fromURL(getClass.getResource("usage.txt")).getLines().mkString("\n")

  def buildBlogListMarkDown(blogs: Seq[File], currentDir: File = File(".")): String = {
    blogs
      .filter(!_.isDirectory)
      .map { f ⇒ (f, f.lines.take(1).map(_.replaceAll("""^#+\s+""", ""))) }
      .map { case (f, tr) ⇒ s"+ [${f.name} ${tr.mkString}](${currentDir.relativize(f).toString})" }
      .mkString("\n")
  }

  def writeIndex(blogs: Seq[File], config: CommandLineConfig): File = {
    val markdown =
      config.indexTemplate.toScala.lines
        .map { s ⇒
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
    val markdowns = config.blogsDir.toScala
      .listRecursively
      .filter(!_.isDirectory)
      .filter(f ⇒ f.extension.contains(".md"))

    val pegdown = new PegDownProcessor()

    val pairs = markdowns
      .map(f ⇒ (f, config.destDir.toScala / (f.nameWithoutExtension + ".html")))
      .toList

    config.destDir.toScala.createIfNotExists(asDirectory = true, createParents = true)
    pairs.foreach {
      case (from, to) ⇒
        to.createIfNotExists()
          .clear()
          .write(pegdown.markdownToHtml(from.lines.mkString("\n")))
    }

    pairs.map(_._2)
  }

  def run(config: CommandLineConfig): Unit = {
    val markdowns = convertMarkdownToHtml(config)
    writeIndex(markdowns, config)
  }

}