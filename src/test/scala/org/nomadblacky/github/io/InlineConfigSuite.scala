package org.nomadblacky.github.io

import org.nomadblacky.github.io.model.InlineConfig
import org.scalatest.FunSuite

/**
  * Created by blacky on 17/05/24.
  */
class InlineConfigSuite extends FunSuite {

  val except = Seq(
    """title: aaa""",
    """value1 : bb bb""",
    """value2 :123""",
    """  value3:\\ddd//"""
  )

  test("Test InlineConfig.getStatement 01") {
    val source =
      """===config===
        |title: aaa
        |value1 : bb bb
        |value2 :123
        |  value3:\\ddd//
        |===end config===
        |# hoge
        |
        |foo
        |bar
      """.stripMargin
    assert(InlineConfig.getStatement(source.split("\n")) == except)
  }

  test("Test InlneConfig.getStatement 02") {
    val source =
      """
        |# hoge
        |
        |===config===
        |title: aaa
        |value1 : bb bb
        |value2 :123
        |  value3:\\ddd//
        |===end config===
        |foo
        |bar
      """.stripMargin
    assert(InlineConfig.getStatement(source.split("\n")) == except)
  }
}
