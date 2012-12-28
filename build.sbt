name := "OCD-Transform"

organization := "hr.element.etb"

version := "0.0.0-SNAPSHOT"

crossScalaVersions := Seq("2.10.0-RC5")

scalaVersion <<= crossScalaVersions(_.head)

scalacOptions := Seq(
  "-unchecked"
, "-deprecation"
, "-encoding", "UTF-8"
, "-optimise"
, "-Xno-uescape"
, "-feature"
, "-language:postfixOps", "-language:implicitConversions", "-language:existentials"
)

unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(_ :: Nil)

unmanagedSourceDirectories in Test    <<= (scalaSource in Test)(_ :: Nil)

libraryDependencies <++= scalaVersion(sV => Seq(
  "org.scala-lang" % "scala-swing" % sV
, "com.typesafe.akka" %% "akka-actor" % "2.1.0"
))

resolvers := Seq("Element Nexus" at "http://repo.element.hr/nexus/content/groups/public/" )

externalResolvers <<= resolvers map(r =>
  Resolver.withDefaultResolvers(r, mavenCentral = false)
)

seq(dependencyReportSettings: _*)
