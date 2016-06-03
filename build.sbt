fork := true

scalaVersion := "2.11.8"

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += Resolver.url(
  "sbt-plugin-releases",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/")
)(Resolver.ivyStylePatterns)

// need scalatest also as a build dependency: the build implements a custom reporter
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6"

libraryDependencies += "junit" % "junit" % "4.12" % "test"

libraryDependencies += "com.github.fellowship_of_the_bus" %% "fellowship-of-the-bus-slick2d-lib" % "0.1-SNAPSHOT" changing()

libraryDependencies += "com.github.fellowship_of_the_bus" %% "fellowship-of-the-bus-lib" % "0.4-SNAPSHOT" changing()

scalacOptions ++= Seq("-deprecation", "-feature", "-optimize", "-Yinline-warnings")

val os = System.getProperty("os.name").split(" ")(0).toLowerCase match {
  case "linux" => "linux"
  case "mac" => "macosx"
  case "windows" => "windows"
  case "sunos" => "solaris"
  case x => x
}

val separator = System.getProperty("os.name").split(" ")(0).toLowerCase match {
  case "linux" => ":"
  case "mac" => ":"
  case "windows" => ";"
  case "sunos" => ":"
  case x => ":"
}

javaOptions += "-Djava.library.path=" + System.getProperty("java.library.path") +
  separator + "./src/main/resources/natives/" + os
