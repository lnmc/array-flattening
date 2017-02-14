
name := "array-flattening"

organization := "hr.lukanimac"

version := "0.2"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.scalatest"   %% "scalatest"    % "3.0.1"   % "test",
  "org.scalacheck"  %% "scalacheck"   % "1.13.4"  % "test"
)


scalacOptions ++= List("-feature","-deprecation", "-unchecked", "-Xlint")
