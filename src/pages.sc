// Dependencies
import $ivy.`org.pegdown:pegdown:1.6.0`
import $ivy.`com.lihaoyi::scalatags:0.6.7`

import ammonite.ops._

import org.pegdown.PegDownProcessor

import scalatags.Text.all._
import scalatags.Text.tags2


// Pages
trait PageResource extends ConcreteHtmlTag[String]
type PageContent = ConcreteHtmlTag[String]

trait Page {
  def scalatags(): ConcreteHtmlTag[String]
  def rawString(): String = scalatags.toString
}

trait BasicPage extends Page {
  def title(): String
  def resources(): Seq[PageResource]
  def content(): Seq[PageContent]

  override def scalatags(): ConcreteHtmlTag[String] =
    html(
      head(
        (tags2.title(title) +: resources): _*
      ),
      body(
        content
      )
    )
}

case class BasicPageImpl(
  title: String,
  resources: Seq[PageResource],
  content: Seq[PageContent]
) extends BasicPage


// Page contents
val globalHeader = header(
  div(
    a(href:="/")(
      p("nomadblacky.github.io")
    )
  )
)

val globalFooter = footer(
  div(
    a(href:="https://github.com/NomadBlacky/nomadblacky.github.io")(
      p("Feedback")
    )
  )
)


// Page builder
trait PageBuilder[T] extends (T => Page)

trait PegDown {
  val pegdownOptions: Int = 0
  val pegdown: PegDownProcessor = new PegDownProcessor(pegdownOptions)
}

val indexPageBuilder = new PageBuilder[Path] with PegDown {
  def apply(path: Path): Page = BasicPageImpl(
    "nomadblacky.github.io",
    Seq(),
    Seq(
      globalHeader,
      div(raw(pegdown.markdownToHtml(read! path))),
      globalFooter
    )
  )
}

val postsListPageBuilder = new PageBuilder[Seq[Path]] {
  def apply(blogs: Seq[Path]): Page = BasicPageImpl(
    "blogs",
    Seq(),
    Seq(
      globalHeader,
      div(
        ul(
          li("a"),
          li("b"),
          li("c")
        )
      ),
      globalFooter
    )
  )
}
