package org.nomadblacky.github.io

import java.io.{ File, PrintWriter }

/**
 * Created by blacky on 17/05/10.
 */
object DeployBlogs extends App {
  val index = new File("index.md")
  if (!index.exists()) {
    val pw = new PrintWriter(index)
    pw.println("::articles::")
  }
}
