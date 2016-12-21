name := "ImageClassification"

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.0.0" % "provided",
  "org.apache.spark" %% "spark-streaming" % "2.0.0",
  "org.apache.spark" %% "spark-mllib" % "2.0.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.0.0"
)
