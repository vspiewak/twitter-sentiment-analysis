Twitter Sentiment Analysis
==========================

Retrieve tweets using Spark Streaming,    
language detection & sentiment analysis (StanfordNLP),    
live dashboard using Kibana.
Ingest the tweets to MapR-DB.

Launch:

    curl -O https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-1.4.4.tar.gz
    tar xvzf elasticsearch-1.4.4.tar.gz
    cd elasticsearch-1.4.4
    bin/plugin -install mobz/elasticsearch-head
    bin/elasticsearch -d

    cd ..
    chmod a+x insert.dashboard.sh
    ./insert.dashboard.sh

    curl -O https://download.elastic.co/kibana/kibana/kibana-4.0.0-linux-x64.tar.gz
    tar xvzf kibana-4.0.0-darwin-x64.tar.gz
    cd kibana-4.0.0-darwin-x64
    bin/kibana

    cd ..

    hbase shell
    create 'twitter_sentiment', 'TwitterSentiment'

    JAVA_OPTS=-Xmx2G sbt assembly

    ../spark-1.4.1/bin/spark-submit \
    --class com.github.vspiewak.TwitterSentimentAnalysis \
    --driver-class-path `hbase classpath` \
    --master local[2] \
    target/scala-2.10/twitter-sentiment-analysis-assembly-0.1-SNAPSHOT.jar \
    <consumer_key> \
    <consumer_secret> \
    <access_token> \
    <access_token_secret> \
    [<filters>]
