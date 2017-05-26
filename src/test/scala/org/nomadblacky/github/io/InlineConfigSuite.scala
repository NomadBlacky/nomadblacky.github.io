package org.nomadblacky.github.io

import org.nomadblacky.github.io.model.InlineConfig
import org.scalatest.FunSuite

/**
  * Created by blacky on 17/05/24.
  */
class InlineConfigSuite extends FunSuite {

  val source1 =
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

  val source2 =
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

  val exceptStatement = Seq(
    """title: aaa""",
    """value1 : bb bb""",
    """value2 :123""",
    """  value3:\\ddd//"""
  )

  test("Test InlineConfig.getStatement 01") {
    assert(InlineConfig.getStatement(source1.split("\n")) == exceptStatement)
  }

  test("Test InlneConfig.getStatement 02") {
    assert(InlineConfig.getStatement(source2.split("\n")) == exceptStatement)
  }

  test("Test InlineConfig.read 01") {
  }
}
