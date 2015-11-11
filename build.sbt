//libraryDependencies += "com.mapr.fs" % "mapr-hbase" % "5.0.0-mapr"

libraryDependencies += "org.apache.hbase" % "hbase-common" % "0.98.12-mapr-1506-m7-5.0.0"

libraryDependencies += "org.apache.hbase" % "hbase-client" % "0.98.12-mapr-1506-m7-5.0.0"

libraryDependencies += "org.apache.hbase" % "hbase-server" % "0.98.12-mapr-1506-m7-5.0.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.1.0" % "provided"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.1.0" % "provided"

libraryDependencies += "org.apache.spark" %% "spark-streaming-twitter" % "1.2.0"

libraryDependencies += "org.twitter4j" % "twitter4j-stream" % "3.0.3"

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.1"

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.1" classifier "models"

libraryDependencies += "org.elasticsearch" % "elasticsearch-spark_2.10" % "2.1.0.Beta3"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"

resolvers += "MapR Repo" at "http://repository.mapr.com/maven/"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
