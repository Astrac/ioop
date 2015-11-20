package astrac.ioop

object dsl {
  import cats.free._
  import java.io.FileNotFoundException
  import java.nio.file.FileAlreadyExistsException
  import java.nio.file.Path

  sealed trait CreateMode

  object CreateMode {
    case object FailIfExists extends CreateMode
    case object IgnoreIfExists extends CreateMode
    case object OverwriteIfExists extends CreateMode
  }

  sealed trait DeleteMode

  object DeleteMode {
    case object FailIfAbsent extends DeleteMode
    case object SucceedIfAbsent extends DeleteMode
  }

  case class FileResource(file: Path)

  type Ioop[T] = Free[IoopAst, T]

  def value[T](content: T): Ioop[T] =
    Free.liftF(Value(content))

  def exists(path: Path): Ioop[Boolean] =
    Free.liftF(FileExists(path))

  def failure(ex: Throwable): Ioop[Nothing] =
    Free.liftF(IoFailure(ex))

  def create(path: Path, mode: CreateMode = CreateMode.FailIfExists): Ioop[Unit] = mode match {

    case CreateMode.FailIfExists => for {
      e <- exists(path)
      c <- if (!e) Free.liftF(CreateFile(path)) else failure(new FileAlreadyExistsException(path.toString))
    } yield c

    case CreateMode.IgnoreIfExists => for {
      e <- exists(path)
      c <- if (!e) Free.liftF(CreateFile(path)) else value(())
    } yield c

    case CreateMode.OverwriteIfExists => for {
      d <- delete(path, DeleteMode.SucceedIfAbsent)
      c <- Free.liftF(CreateFile(path))
    } yield c
  }

  def delete(path: Path, mode: DeleteMode = DeleteMode.FailIfAbsent): Ioop[Unit] = mode match {

    case DeleteMode.FailIfAbsent => for {
      e <- exists(path)
      d <- if (e) Free.liftF(DeleteFile(path)) else failure(new FileNotFoundException(path.toString))
    } yield d

    case DeleteMode.SucceedIfAbsent => for {
      e <- exists(path)
      d <- if (!e) Free.liftF(DeleteFile(path)) else value(())
    } yield d
  }

  def write(path: Path, data: Iterable[String]): Ioop[Unit] = Free.liftF(WriteFile(path, data))

  def read[Out](path: Path, handler: Iterable[String] => Out): Ioop[Out] = Free.liftF(ReadFile(path, handler))

  def transform(source: Path, target: Path, handler: Iterable[String] => Iterable[String]): Ioop[Unit] = for {
    d <- read(source, handler)
    _ <- write(target, d)
  } yield ()
}
