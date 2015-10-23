package astrac.ioop

import java.nio.file.Path

sealed trait IoopAst[+T]

case class Value[T](content: T) extends IoopAst[T]

case class IoFailure(ex: Throwable) extends IoopAst[Nothing]

case class FileExists(path: Path) extends IoopAst[Boolean]

case class CreateFile(path: Path) extends IoopAst[Unit]

case class DeleteFile(path: Path) extends IoopAst[Unit]

// case class CopyFile(src: Path, dest: Path) extends IoopM[Unit]

// case class MoveFile(src: Path, dest: Path) extends IoopM[Unit]
