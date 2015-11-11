package com.github.vspiewak

import java.time.format.DateTimeFormatter

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf

import com.cybozu.labs.langdetect.DetectorFactory
import com.github.vspiewak.util.LogUtils
import com.github.vspiewak.util.SentimentAnalysisUtils._
import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.elasticsearch.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.PairRDDFunctions

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
                                                                        // can use them to generate OAuth credentials
                                                                             System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
                                                                                  System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
                                                                                       System.setProperty("twitter4j.oauth.accessToken", accessToken)
                                                                                            System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

                                                                                                 val sparkConf = new SparkConf().setAppName("TwitterSentimentAnalysis")

                                                                                                        val ssc = new StreamingContext(sparkConf, Seconds(1))

                                                                                                             val tweets = TwitterUtils.createStream(ssc, None, filters)

                                                                                                                  tweets.print()

                                                                                                                    //set JobConfiguration variables for writing to HBase
                                                                                                                        val tableName = "twitter_sentiment"
                                                                                                                            val cfNameBytes = Bytes.toBytes("TwitterSentiment")

                                                                                                                                  val conf = HBaseConfiguration.create()
                                                                                                                                      val jobConfig: JobConf = new JobConf(conf, this.getClass)
                                                                                                                                          jobConfig.set("mapreduce.output.fileoutputformat.outputdir", "/user/user01/out")
                                                                                                                                            jobConfig.setOutputFormat(classOf[TableOutputFormat])
                                                                                                                                                jobConfig.set(TableOutputFormat.OUTPUT_TABLE, tableName)

                                                                                                                                                     /*tweets.foreachRDD{(rdd, time) =>
                                                                                                                                                            rdd.map(t => {
                                                                                                                                                                         Map(
                                                                                                                                                                                      "user"-> t.getUser.getScreenName,
                                                                                                                                                                                                 "created_at" -> t.getCreatedAt.toInstant.toString,
                                                                                                                                                                                                            "location" -> Option(t.getGeoLocation).map(geo => { s"${geo.getLatitude},${geo.getLongitude}" }),
                                                                                                                                                                                                                     "text" -> t.getText,
                                                                                                                                                                                                                                "hashtags" -> t.getHashtagEntities.map(_.getText),
                                                                                                                                                                                                                                           "retweet" -> t.getRetweetCount,
                                                                                                                                                                                                                                                      "language" -> detectLanguage(t.getText),
                                                                                                                                                                                                                                                                 "sentiment" -> detectSentiment(t.getText).toString
                                                                                                                                                                                                                                                                          )
                                                                                                                                                                                                                                                                               }).saveToEs("twitter/tweet")
                                                                                                                                                                                                                                                                                 }*/

                                                                                                                                                                                                                                                                                     // Write to MapR-DB
                                                                                                                                                                                                                                                                                          tweets.foreachRDD{(rdd, time) =>
                                                                                                                                                                                                                                                                                                 rdd.map(t => {
                                                                                                                                                                                                                                                                                                              var key = t.getUser.getScreenName + "-" + t.getCreatedAt.toInstant.toString
                                                                                                                                                                                                                                                                                                                       var p = new Put(Bytes.toBytes(key))

                                                                                                                                                                                                                                                                                                                                  p.add(cfNameBytes, Bytes.toBytes("user"), Bytes.toBytes(t.getUser.getScreenName))
                                                                                                                                                                                                                                                                                                                                         p.add(cfNameBytes, Bytes.toBytes("created_at"), Bytes.toBytes(t.getCreatedAt.toInstant.toString))
                                                                                                                                                                                                                                                                                                                                                  p.add(cfNameBytes, Bytes.toBytes("location"), Bytes.toBytes(Option(t.getGeoLocation).map(geo => { s"${geo.getLatitude},${geo.getLongitude}" }).toString))
                                                                                                                                                                                                                                                                                                                                                         p.add(cfNameBytes, Bytes.toBytes("text"), Bytes.toBytes(t.getText))
                                                                                                                                                                                                                                                                                                                                                                  p.add(cfNameBytes, Bytes.toBytes("hashtags"), Bytes.toBytes(t.getHashtagEntities.map(_.getText).toString))
                                                                                                                                                                                                                                                                                                                                                                           p.add(cfNameBytes, Bytes.toBytes("retweet"), Bytes.toBytes(t.getRetweetCount))
                                                                                                                                                                                                                                                                                                                                                                                    p.add(cfNameBytes, Bytes.toBytes("language"), Bytes.toBytes(detectLanguage(t.getText)))
                                                                                                                                                                                                                                                                                                                                                                                             p.add(cfNameBytes, Bytes.toBytes("sentiment"), Bytes.toBytes(detectSentiment(t.getText).toString))
                                                                                                                                                                                                                                                                                                                                                                                                      (new ImmutableBytesWritable, p)
                                                                                                                                                                                                                                                                                                                                                                                                             }).saveAsHadoopDataset(jobConfig)
                                                                                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                                                                                     ssc.start()
                                                                                                                                                                                                                                                                                                                                                                                                                          ssc.awaitTermination()

                                                                                                                                                                                                                                                                                                                                                                                                                             }

                                                                                                                                                                                                                                                                                                                                                                                                                               def detectLanguage(text: String) : String = {

                                                                                                                                                                                                                                                                                                                                                                                                                                     Try {
                                                                                                                                                                                                                                                                                                                                                                                                                                             val detector = DetectorFactory.create()
                                                                                                                                                                                                                                                                                                                                                                                                                                                     detector.append(text)
                                                                                                                                                                                                                                                                                                                                                                                                                                                         detector.detect()
                                                                                                                                                                                                                                                                                                                                                                                                                                                             }.getOrElse("unknown")

                                                                                                                                                                                                                                                                                                                                                                                                                                                               }

                                                                                                                                                                                                                                                                                                                                                                                                                                                                }

