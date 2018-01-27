// Dependencies
import $ivy.`org.pegdown:pegdown:1.6.0`
import $ivy.`com.lihaoyi::scalatags:0.6.7`

import $file.src.pages

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
  markdowns
    .map(m => (m, pegdown.markdownToHtml(read! m)))
    .map { case (m, h) =>
      val page = html(
        head(
          tags2.title(m.last)
        ),
        body(
          raw(h)
        )
      )
      write.over(pagesDir/(m.last + ".html"), page.toString)
    }

  write.over(pwd/"index.html", buildIndexPage(pwd/"index.md"))

  println("Complete!")
}

def buildIndexPage(markdown: Path): String = {
  html(
    head(
      tags2.title("nomadblacky.github.io")
    ),
    body(
      raw(pegdown.markdownToHtml(read! markdown))
    )
  ).toString
}
