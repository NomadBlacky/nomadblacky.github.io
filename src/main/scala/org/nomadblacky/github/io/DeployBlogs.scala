package org.nomadblacky.github.io

import java.io.FileNotFoundException

import better.files._
import org.pegdown.PegDownProcessor

import scala.io.Source
import scala.util.Try

/**
 * Created by blacky on 17/05/10.
 */
object DeployBlogs {

  def getFile(pathFunc: ⇒ String, isDirectory: Boolean): Try[File] = Try {
    val f = File(pathFunc)
    if (!f.exists) {
      throw new FileNotFoundException(s"${f.path} is not found.")
    }
    if (f.isDirectory ^ isDirectory) {
      val msg = if (f.isDirectory) "directory" else "file"
      throw new IllegalStateException(s"${f.path} is a ${msg}")
    }
    f
  }

  def getUsage(): String = {
    Source.fromURL(getClass.getResource("usage.txt")).getLines().mkString("\n")
  }

  def buildBlogListMarkDown(blogs: Seq[File], currentDir: File = File(".")): String = {
    blogs
      .filter(!_.isDirectory)
      .map { f ⇒ (f, f.lines.take(1).map(_.replaceAll("""^#+\s+""", ""))) }
      .map { case (f, tr) ⇒ s"+ [${f.name} ${tr.mkString}](${currentDir.relativize(f).toString})" }
      .mkString("\n")
  }

  def writeIndex(blogs: Seq[File], templateFile: File, indexFile: File = File("index.md")) = {
    val markdown =
      templateFile.lines
        .map { s ⇒
          if (s.matches("""^::articles::$"""))
            buildBlogListMarkDown(blogs)
          else
            s
        }

    indexFile
      .createIfNotExists()
      .clear()
      .append(markdown.mkString("\n"))
  }

  def convertMarkdownToHtml(baseDir: File, destDir: File): List[File] = {
    val markdowns = baseDir
      .listRecursively
      .filter(!_.isDirectory)
      .filter(f ⇒ f.extension.map(_ == ".md").getOrElse(false))

    val pegdown = new PegDownProcessor()

    val pairs = markdowns
      .map(f ⇒ (f, destDir / (f.nameWithoutExtension + ".html")))
      .toList

    destDir.createIfNotExists(true, true)
    pairs.foreach {
      case (from, to) ⇒
        to.createIfNotExists()
          .clear()
          .write(pegdown.markdownToHtml(from.lines.mkString("\n")))
    }

    pairs.map(_._2)
  }

  def run(args: Array[String]): Unit = {
    val templateFile = getFile(args(0), false).get
    val blogsDir = getFile(args(1), true).get
    val destDir = getFile(args(2), false).getOrElse(File("_build"))

    val markdowns = convertMarkdownToHtml(blogsDir, destDir)
    writeIndex(markdowns, templateFile)
  }

  def main(args: Array[String]): Unit = {
    if (args.size == 0) println(getUsage())
    else run(args)
  }
}