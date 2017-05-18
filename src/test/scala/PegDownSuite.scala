import org.pegdown.PegDownProcessor
import org.scalatest.FunSuite

/**
  * Created by blacky on 17/05/18.
  */
class PegDownSuite extends FunSuite {

  test("pegdown") {
    val processer = new PegDownProcessor()
    val source =
      """
        |# h1
        |## h2
        |### h3
      """.stripMargin
    val expect =
      """<h1>h1</h1>
        |<h2>h2</h2>
        |<h3>h3</h3>""".stripMargin
    assert(processer.markdownToHtml(source) == expect)
  }
}
