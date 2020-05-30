name := "typed-player"

version := "0.1"

scalaVersion := "2.13.2"
val akkaVersion = "2.6.5"
val scalaTestVersion = "3.1.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
)

wartremoverErrors in (Compile, compile) ++= Warts.unsafe
wartremoverErrors in (Test, test) ++= Warts.allBut(Wart.NonUnitStatements)
