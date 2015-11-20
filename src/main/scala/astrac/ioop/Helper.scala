package astrac.ioop

import java.nio.file._
import scala.collection.convert.WrapAsJava
import scala.collection.convert.WrapAsScala
import scala.util.Try

object Helper {
  def exists(p: Path, followLinks: Boolean = true) =
    if (followLinks) Files.exists(p)
    else Files.exists(p, LinkOption.NOFOLLOW_LINKS)

  def create(p: Path): Try[Unit] = Try { Files.createFile(p); () }

  def delete(p: Path): Try[Unit] = Try { Files.delete(p) }

  def read[T](p: Path, handler: Iterable[String] => T): Try[T] = Try {
    handler(WrapAsScala.asScalaIterator(Files.lines(p).iterator()).toIterable)
  }

  def write(p: Path, data: Iterable[String]): Try[Unit] = Try {
    Files.write(p, WrapAsJava.asJavaIterable(data))
    ()
  }
}
