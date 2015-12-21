FROM java:8
MAINTAINER vspiewak


WORKDIR /opt
ENV SPARK_VERSION 1.5.2
RUN curl -o spark.tgz http://d3kbcqa49mib13.cloudfront.net/spark-$SPARK_VERSION.tgz
RUN tar xvzf spark.tgz
RUN rm spark.tgz
RUN ln -s spark-$SPARK_VERSION spark
WORKDIR spark
RUN sbt/sbt assembly


WORKDIR /opt
RUN curl -L -o sbt.tgz https://dl.bintray.com/sbt/native-packages/sbt/0.13.9/sbt-0.13.9.tgz
RUN tar xvzf sbt.tgz
RUN rm sbt.tgz
ENV PATH /opt/sbt/bin:$PATH


RUN mkdir /opt/app
WORKDIR /opt/app
ADD build.sbt ./build.sbt
ADD src ./src
ADD project ./project 
ADD lib ./lib

RUN JAVA_OPTS=-Xmx2G sbt assembly
RUN cp target/scala-2.10/*-assembly-*.jar app.jar

EXPOSE 4040

ENTRYPOINT [ "/opt/spark/bin/spark-submit", "--conf", "spark.es.nodes=elasticsearch:9200", "--class", "com.github.vspiewak.TwitterSentimentAnalysis", "--master", "local[2]", "/opt/app/app.jar" ]
