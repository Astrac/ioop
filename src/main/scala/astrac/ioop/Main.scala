package astrac.ioop

import java.nio.file.Paths
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

object Main extends App {
  import dsl._

  implicit val ec = ExecutionContext.global

  val test: Ioop[Boolean] = for {
    f <- value(Paths.get("/tmp/foo"))
    e <- exists(f)
    _ = println(s"Exists: $e")
    _ <- if (e) delete(f) else create(f)
    r <- exists(f)
    w <- write(f, "foobar" :: "baz" :: Nil)
  } yield r

  println("\nSync run")
  val r0: Boolean = Filesystem.unsafe.run(test)
  println(s"Sync Result: $r0")

  println("\nAsync run")
  val r1: Future[Boolean] = Filesystem.async.run(test)
  r1.foreach(r => println(s"Async Result: $r"))
}
