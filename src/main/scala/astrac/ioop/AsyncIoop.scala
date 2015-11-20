package astrac.ioop

import cats.{~>, Id}
import java.nio.file.Files
import scala.concurrent.{ExecutionContext, Future, blocking}
import scala.util.{Failure, Success, Try}

class AsyncIoop(implicit ec: ExecutionContext) extends (IoopAst ~> Future) {
  implicit class FutureTryOps[T](f: Future[Try[T]]) {
    def consumeTry: Future[T] = f.flatMap {
      case Success(v) => Future.successful(v)
      case Failure(ex) => Future.failed(ex)
    }
  }

  def apply[T](op: IoopAst[T]): Future[T] = op match {
    case Value(c)       => Future.successful(c)

    case FileExists(p) => Future(blocking(Helper.exists(p)))

    case CreateFile(p) => Future(blocking(Helper.create(p))).consumeTry

    case DeleteFile(p) => Future(blocking(Helper.delete(p))).consumeTry

    case ReadFile(p, h) => Future(blocking(Helper.read(p, h))).consumeTry

    case WriteFile(p, d) => Future(blocking(Helper.write(p, d))).consumeTry

    case IoFailure(ex) => Future.failed(ex)
  }
}

object AsyncIoop {
  implicit def fromExecutionContext(implicit ec: ExecutionContext) =
    new AsyncIoop()
}
