name := "kviz"
scalaVersion  := "2.12.1"
scalacOptions := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

unmanagedBase := baseDirectory.value / "libs"

libraryDependencies ++= {

	Seq(
		"org.scalafx" %% "scalafx" % "8.0.102-R11",
		"org.scala-lang.modules" %% "scala-xml" % "1.0.6",
		"ch.qos.logback" % "logback-classic" % "1.1.7",
		"org.controlsfx" % "controlsfx" % "8.20.8"
	)
}