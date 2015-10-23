package astrac.ioop

import cats.{~>, Id, Monad}
import dsl._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

trait Filesystem[M[_]] {
  implicit def monad: Monad[M]
  def interpreter: IoopAst ~> M
  def run[T](op: Ioop[T]): M[T] = op.foldMap(interpreter)
}

object Filesystem {
  def async(implicit ec: ExecutionContext) = new Filesystem[Future] {
    implicit def monad = implicitly[Monad[Future]]
    lazy val interpreter = AsyncIoop.fromExecutionContext
  }

  def unsafe = new Filesystem[Id]  {
    implicit def monad = implicitly[Monad[Id]]
    lazy val interpreter = UnsafeIoop
  }
}
