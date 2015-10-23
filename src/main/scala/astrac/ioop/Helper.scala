package astrac.ioop

import java.nio.file._
import scala.util.Try

object Helper {
  def exists(p: Path, followLinks: Boolean = true) =
    if (followLinks) Files.exists(p)
    else Files.exists(p, LinkOption.NOFOLLOW_LINKS)

  def create(p: Path): Try[Unit] = Try { Files.createFile(p); () }

  def delete(p: Path): Try[Unit] = Try { Files.delete(p) }
}
