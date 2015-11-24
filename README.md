Twitter Sentiment Analysis
==========================

Retrieve tweets using Spark Streaming,    
language detection & sentiment analysis (StanfordNLP),    
live dashboard using Kibana.
Ingest the tweets to MapR-DB.

Launch:

    # Download and Install Elasticsearch
    curl -O https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-1.4.4.tar.gz
    tar xvzf elasticsearch-1.4.4.tar.gz
    cd elasticsearch-1.4.4
    bin/plugin -install mobz/elasticsearch-head
    # Start Elasticsearch
    bin/elasticsearch -d

    # Create the twitter index in Elasticsearch
    cd ..
    chmod a+x insert.dashboard.sh
    ./insert.dashboard.sh

    # Download and Install Kibana
    curl -O https://download.elastic.co/kibana/kibana/kibana-4.0.0-linux-x64.tar.gz
    tar xvzf kibana-4.0.0-linux-x64.tar.gz
    cd kibana-4.0.0-linux-x64
    # Start Kibana
    bin/kibana

    cd ..

    # Compile the Twitter Sentiment Analysis jar
    JAVA_OPTS=-Xmx2G sbt assembly

    # Create a table in MapR-DB to store the Twitter messages plus the sentiment analysis result
    su - mapr
    hbase shell
    create 'twitter_sentiment', 'TwitterSentiment'

    # Launch the Twitter capture and store the messages in MapR-DB & Elasticsearch
    su - mapr
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
