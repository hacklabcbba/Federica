import sbt._
import sbt.Keys._

object LiftProjectBuild extends Build {

  import Dependencies._
  import BuildSettings._

  lazy val root = Project("federica", file("."))
    .settings(liftAppSettings: _*)
    .settings(libraryDependencies ++=
      compile(
        liftWebkit,
        liftMongodb,
        liftExtras,
        liftMongoauth,
        logback,
        rogueField,
        rogueCore,
        rogueLift,
        rogueIndex,
        imaging,
        angularJs,
        liftNg,
        reCaptcha,
        finagleHttp,
        liftFoBo
      ) ++
      test(scalatest) ++
      container(jettyWebapp)
    )
}
