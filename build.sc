// Dependencies
import $ivy.`org.pegdown:pegdown:1.6.0`
import $ivy.`com.lihaoyi::scalatags:0.6.7`

import $file.src.pages, pages._

import ammonite.ops._
import org.pegdown.PegDownProcessor
import scalatags.Text.all._
import scalatags.Text.tags2

val pagesDir  = pwd/'pages
val postsDir = pwd/'posts

val pegdown = new PegDownProcessor()

@main
def main() = {
  val (markdowns, otherFiles) = ls! postsDir partition (_.ext == "md")

  val posts = markdowns
    .map(m => {
      val rawMarkdown = read! m
      PostInfo(
        title   = extractTitle(rawMarkdown).getOrElse("Untitled"),
        rawHtml = pegdown.markdownToHtml(rawMarkdown),
        dest    = pagesDir/(m.last + ".html")
      )
    })

  // Write index page.
  write.over(pwd/"index.html", indexPageBuilder(pwd/"index.md").rawString)
  println(s"write: ${pwd/"index.html"}")

  // Write blogs list page.
  write.over(pwd/'pages/"blogs.html", postsListPageBuilder(posts, pagesDir).rawString)
  println(s"write: ${pwd/'pages/"blogs.html"}")

  // Write blog view page.
  posts.foreach { p =>
    write.over(p.dest, postViewPageBuilder(p).rawString)
    println(s"write: ${p.dest}")
  }

  println("Complete!")
}

val titleRegex = """^#\s+(.+)$""".r

def extractTitle(rawMarkdown: String): Option[String] =
  rawMarkdown.lines.collectFirst {
    case titleRegex(title) => title
  }
