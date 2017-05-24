package org.nomadblacky.github.io

import org.nomadblacky.github.io.model.InlineConfig
import org.scalatest.FunSuite

/**
  * Created by blacky on 17/05/24.
  */
class InlineConfigSuite extends FunSuite {

  test("Test InlineConfig.getStatement") {
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
    val except = Seq(
      """title: aaa""",
      """value1 : bb bb""",
      """value2 :123""",
      """  value3:\\ddd//"""
    )

    assert(InlineConfig.getStatement(source.split("\n")) == except)
  }
}
