name         := "ioop"
scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV       = "2.4.0"
  val akkaStreamV = "1.0"
  val scalaTestV  = "2.2.4"
  Seq(
    "com.chuusai"        %% "shapeless"                         % "2.2.5",
    "com.typesafe.akka"  %% "akka-actor"                        % akkaV,
    "com.typesafe.akka"  %% "akka-testkit"                      % akkaV        % "test",
    "com.typesafe.akka"  %% "akka-stream-experimental"          % akkaStreamV,
    "org.scalatest"      %% "scalatest"                         % scalaTestV   % "test",
    "org.spire-math"     %% "cats"                              % "0.3.0"
  )
}
