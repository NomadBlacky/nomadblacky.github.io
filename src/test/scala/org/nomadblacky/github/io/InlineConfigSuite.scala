package org.nomadblacky.github.io

import org.nomadblacky.github.io.model.InlineConfig
import org.scalatest.FunSuite

/**
 * Created by blacky on 17/05/24.
 */
class InlineConfigSuite extends FunSuite {

  val source1: String =
    """$=====
      |title: aaa
      |value1 : bb bb
      |value2 :123
      |  value3:\\ddd//
      |$=====
      |# hoge
      |
      |foo
      |bar
    """.stripMargin

  val source2: String =
    """
      |# hoge
      |
      |$==========
      |title: aaa
      |value1 : bb bb
      |value2 :123
      |  value3:\\ddd//
      |$==========
      |foo
      |bar
    """.stripMargin

  val source3: String =
    """# hoge
      |foo
      |bar
    """.stripMargin

  val exceptStatement: Seq[String] = Seq(
    """title: aaa""",
    """value1 : bb bb""",
    """value2 :123""",
    """  value3:\\ddd//"""
  )

  val exceptConfig: Map[String, InlineConfig] = Map(
    "title" → InlineConfig("title", "aaa"),
    "value1" → InlineConfig("value1", "bb bb"),
    "value2" → InlineConfig("value2", "123"),
    "value3" → InlineConfig("value3", """\\ddd//""")
  )

  test("InlineConfig.getStatement 01") {
    assert(InlineConfig.getStatement(source1.split("\n")) == exceptStatement)
  }

  test("InlneConfig.getStatement 02") {
    assert(InlineConfig.getStatement(source2.split("\n")) == exceptStatement)
  }

  test("InlineConfig.getStatement 03") {
    assert(InlineConfig.getStatement(source3.split("\n")) == Seq())
  }

  test("Test InlineConfig.read 01") {
    assert(InlineConfig.read(source1.split("\n")) == exceptConfig)
  }

}
