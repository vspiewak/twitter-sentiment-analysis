Twitter Sentiment Analysis
==========================

Retrieve tweets using Spark Streaming,    
language detection & sentiment analysis (StanfordNLP),    
live dashboard using Kibana.

Launch:

    curl -O http://mir2.ovh.net/ftp.apache.org/dist/spark/spark-1.1.0/spark-1.1.0.tgz"
    tar xvzf spark-1.1.0.tgz
    cd spark-1.1.0
    ./sbt/sbt assembly
    
    cd ..

    curl -O https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-1.4.4.tar.gz
    tar xvzf elasticsearch-1.4.4.zip
    cd elasticsearch-1.4.4
    bin/plugin -install mobz/elasticsearch-head
    bin/elasticsearch -d

    cd ..
    chmod a+x insert.dashboard.sh
    ./insert.dashboard.sh

    curl -O https://download.elasticsearch.org/kibana/kibana/kibana-4.0.0-darwin-x64.tar.gz
    tar xvzf kibana-4.0.0-darwin-x64.tar.gz
    cd kibana-4.0.0-darwin-x64
    bin/kibana

    cd ..

    JAVA_OPTS=-Xmx2G sbt assembly

    ../spark-1.1.0/bin/spark-submit \
    --class com.github.vspiewak.TwitterSentimentAnalysis \
    --master local[2] \
    target/scala-2.10/twitter-sentiment-analysis-assembly-0.1-SNAPSHOT.jar \
    <consumer_key> \
    <consumer_secret> \
    <access_token> \
    <access_token_secret> \
    [<filters>]
