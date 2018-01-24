// Dependencies
import $ivy.`org.pegdown:pegdown:1.6.0`
import $ivy.`com.lihaoyi::scalatags:0.6.7`

import ammonite.ops._
import org.pegdown.PegDownProcessor
import scalatags.Text.all._
import scalatags.Text.tags2

val docsDir  = pwd/'docs
val postsDir = pwd/'posts

@main
def main() = {
  val (markdowns, otherFiles) = ls! postsDir partition (_.ext == "md")
  val pegdown = new PegDownProcessor()
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
      write(docsDir/(m.last + ".html"), page.toString)
    }
}
