organization := "rever.client4s"

name := "elasticsearch-scala-client-twitter"

version := "2.4.0-2"

scalaVersion := "2.11.8"

libraryDependencies += "org.elasticsearch" % "elasticsearch" % "2.4.0"

libraryDependencies += "com.twitter" %% "util-core" % "6.37.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

resolvers += "Artifactory" at "http://central.rever.vn/artifactory/libs-release-local/"

publishTo := Some("Artifactory Realm" at "http://central.rever.vn/artifactory/libs-release-local")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")