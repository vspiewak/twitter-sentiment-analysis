libraryDependencies += "org.apache.spark" %% "spark-core" % "1.1.0" % "provided"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.1.0" % "provided"

libraryDependencies += "org.apache.spark" %% "spark-streaming-twitter" % "1.2.0"

libraryDependencies += "org.twitter4j" % "twitter4j-stream" % "3.0.3"

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.1"

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.1" classifier "models"

libraryDependencies += "org.elasticsearch" % "elasticsearch-spark_2.10" % "2.2.0-m1" % "compile"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}