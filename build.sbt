
name := "array-flattening"

organization := "hr.lukanimac"

version := "0.1"

scalaVersion := "2.12.1"

resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/releases"
resolvers += Resolver.mavenLocal


libraryDependencies ++= Seq(
  "org.scalatest"   %% "scalatest"    % "3.0.1"   % "test",
  "org.scalacheck"  %% "scalacheck"   % "1.13.4"  % "test",
  "com.storm-enroute" %% "scalameter" % "0.8.2"
)


scalacOptions ++= List("-feature","-deprecation", "-unchecked", "-Xlint")

mainClass in (Compile, run) := Some("hr.lukanimac.ArrayUtils")
