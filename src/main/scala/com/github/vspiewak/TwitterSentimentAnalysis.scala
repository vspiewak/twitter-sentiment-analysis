package com.github.vspiewak

import com.cybozu.labs.langdetect.DetectorFactory
import com.github.vspiewak.util.{LogUtils, SentimentAnalysisUtils}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.util.Try

object TwitterSentimentAnalysis {

   def main(args: Array[String]) {

     if (args.length < 4) {
       System.err.println("Usage: TwitterSentimentAnalysis <consumer key> <consumer secret> " +
         "<access token> <access token secret> [<filters>]")
       System.exit(1)
     }

     LogUtils.setStreamingLogLevels()

     DetectorFactory.loadProfile("src/main/resources/profiles")

     val Array(consumerKey, consumerSecret, accessToken, accessTokenSecret) = args.take(4)
     val filters = args.takeRight(args.length - 4)

     // Set the system properties so that Twitter4j library used by twitter stream
     // can use them to generat OAuth credentials
     System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
     System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
     System.setProperty("twitter4j.oauth.accessToken", accessToken)
     System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

     val sparkConf = new SparkConf().setAppName("TwitterSentimentAnalysis")

     val ssc = new StreamingContext(sparkConf, Seconds(2))

     TwitterUtils.createStream(ssc, None, filters).foreachRDD(tweets => {
       tweets.foreach(t => {
         val language = detectLanguage(t.getText)
         if(language.equals("en")) {
           println(
             "Tweet from: (" + t.getUser.getScreenName + ")" +
             " Language: " + language + " " +
             " Sentiment: " + SentimentAnalysisUtils.detect(t.getText) +
             " Text: " + t.getText + "\n\n"
           )
         }
       })
     })

     ssc.start()
     ssc.awaitTermination()

   }

  def detectLanguage(text: String) : String = {

    Try {
      val detector = DetectorFactory.create()
      detector.append(text)
      detector.detect()
    }.getOrElse("")

  }

 }