package astrac.ioop

import cats.{~>, Id}

object UnsafeIoop extends (IoopAst ~> Id) {
  def apply[T](op: IoopAst[T]): Id[T] = op match {

    case Value(c)       => c

    case FileExists(p) => Helper.exists(p)

    case CreateFile(p) => Helper.create(p).get

    case DeleteFile(p) => Helper.delete(p).get
  }
}
