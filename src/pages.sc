// Dependencies
import $ivy.`org.pegdown:pegdown:1.6.0`
import $ivy.`com.lihaoyi::scalatags:0.6.7`

import ammonite.ops._

import org.pegdown.PegDownProcessor

import scalatags.Text.all._
import scalatags.Text.tags2


// Styles
val styleSheets = Seq(
  "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
)

// Pages
trait PageResource extends ConcreteHtmlTag[String]
type PageContent = ConcreteHtmlTag[String]

trait Page {
  def scalatags(): ConcreteHtmlTag[String]
  def rawString(): String = scalatags.toString
}

trait BasicPage extends Page {
  def title(): String
  def content(): Seq[PageContent]

  override def scalatags(): ConcreteHtmlTag[String] =
    html(
      head(
        tags2.title(title),
        for(s <- styleSheets)
        yield link(href := s, rel := "stylesheet", `type` := "text/css")
      ),
      body(
        content
      )
    )
}

case class BasicPageImpl(
  title: String,
  content: Seq[PageContent]
) extends BasicPage

// Page contents
val globalHeader = header(
  tags2.nav(
    cls:="navbar navbar-expand-lg navbar-light bg-light",
    div(
      a(cls:="navbar-brand", href:="/")("nomadblacky.github.io"),
      cls:="collapse navbar-collapse",
      ul(
        cls:="navbar-nav",
        li(
          cls:="nav-item",
          a(cls:="nav-link", href:="pages/blogs.html")(
            p("Blogs")
          )
        )
      )
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
    Seq(
      globalHeader,
      div(raw(pegdown.markdownToHtml(read! path))),
      globalFooter
    )
  )
}

val postsListPageBuilder = new PageBuilder[(Seq[PostInfo], Path)] {
  def apply(t2: (Seq[PostInfo], Path)): Page = {
    val posts = t2._1
    val baseDir = t2._2
    BasicPageImpl(
      "blogs",
      Seq(
        globalHeader,
        div(
          ul(
            for(p <- posts)
            yield a(href:=p.destRelativeTo(baseDir).toString)(
              li(p.title)
            )
          )
        ),
        globalFooter
      )
    )
  }
}

case class PostInfo(
  title: String,
  rawHtml: String,
  dest: Path
) {
  def destRelativeTo(p: Path): RelPath = dest.relativeTo(p)
}

val postViewPageBuilder = new PageBuilder[PostInfo] {
  def apply(post: PostInfo): Page = BasicPageImpl(
    post.title,
    Seq(
      div(raw(post.rawHtml))
    )
  )
}
