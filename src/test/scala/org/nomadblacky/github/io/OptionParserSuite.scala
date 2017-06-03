package org.nomadblacky.github.io

import java.io.File

import org.scalatest.{FunSuite, Matchers}

/**
  * Created by blacky on 17/06/02.
  */
class OptionParserSuite extends FunSuite with Matchers {
  import OptionParser._

  test("Default values") {
    parser.parse(Seq.empty, CommandLineConfig()) match {
      case Some(c) =>
        c.destDir shouldBe new File("_build")
        c.indexTemplate shouldBe new File("templates/index.md")
        c.blogsDir shouldBe new File("blogs")
        c.indexFile shouldBe new File("index.md")
      case None =>
        fail()
    }
  }

  test("--dest-dir option") {
    val options = Seq("--dest-dir", "dest")
    parser.parse(options, CommandLineConfig()) match {
      case Some(c) =>
        c.destDir shouldBe new File("dest")
      case None =>
        fail()
    }
  }

  test("--index-template option") {
    val options = Seq("--index-template", "template/hoge/template.html")
    parser.parse(options, CommandLineConfig()) match {
      case Some(c) =>
        c.indexTemplate shouldBe new File("template/hoge/template.html")
      case None =>
        fail()
    }
  }

  test("--blogs-dir option") {
    val options = Seq("--blogs-dir", "blogshoge")
    parser.parse(options, CommandLineConfig()) match {
      case Some(c) =>
        c.blogsDir shouldBe new File("blogshoge")
      case None =>
        fail()
    }
  }

}
