Twitter Sentiment Analysis
==========================

Retrieve tweets using Spark Streaming,    
language detection & sentiment analysis (StanfordNLP),    
live dashboard using Kibana.

Launch:

    curl -O http://mir2.ovh.net/ftp.apache.org/dist/spark/spark-1.2.1/spark-1.2.1.tgz
    tar xvzf spark-1.2.1.tgz
    cd spark-1.2.1
    mvn -DskipTests clean package
    
    cd ..

    curl -O https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-1.4.4.tar.gz
    tar xvzf elasticsearch-1.4.4.zip
    cd elasticsearch-1.4.4
    bin/plugin -install mobz/elasticsearch-head
    bin/elasticsearch -d

    cd ..

    JAVA_OPTS=-Xmx2G sbt assembly

    ../spark-1.2.1/bin/spark-submit \
    --class com.github.vspiewak.TwitterSentimentAnalysis \
    --master local[2] \
    target/scala-2.10/TwitterSentimentAnalysis-assembly-1.0.jar \
    <consumer_key> \
    <consumer_secret> \
    <access_token> \
    <access_token_secret> \
    [<filters>]
