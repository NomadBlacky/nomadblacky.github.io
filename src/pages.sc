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
  def build(): ConcreteHtmlTag[String]
}

trait BasicPage extends Page {
  def title(): String
  def resources(): Seq[PageResource]
  def content(): Seq[PageContent]

  override def build(): ConcreteHtmlTag[String] =
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
trait PageBuilder extends (Path => Page)

trait PegDown {
  val pegdownOptions: Int = 0
  val pegdown: PegDownProcessor = new PegDownProcessor(pegdownOptions)
}

val indexPageBuilder = new PageBuilder with PegDown {
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
