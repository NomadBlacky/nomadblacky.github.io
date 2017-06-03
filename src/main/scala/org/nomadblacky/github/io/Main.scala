package org.nomadblacky.github.io

import scala.io.Source

/**
 * Created by blacky on 17/06/02.
 */
object Main {
  lazy val usage: String = Source.fromURL(getClass.getResource("usage.txt")).getLines().mkString("\n")

  def main(args: Array[String]): Unit = {
    import OptionParser._
    parser.parse(args, CommandLineConfig()) match {
      case Some(config) ⇒
        DeployBlogs.run(config)
      case None ⇒
        println(usage)
    }
  }
}
