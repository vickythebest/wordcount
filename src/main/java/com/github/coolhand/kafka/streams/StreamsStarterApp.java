package com.github.coolhand.kafka.streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.util.Arrays;
import java.util.Properties;

/**
 * Hello Kafka Stream!
 *
 */

public class StreamsStarterApp
{
    public Topology createTopology(){

        StreamsBuilder streamsBuilder = new StreamsBuilder();
//        1. Stream
        KStream<String, String> wordCountInput = streamsBuilder.stream("word-count-input");

        wordCountInput.print(Printed.toSysOut());

        var wordCounts = wordCountInput
//        2. Map the key & value in lowercase
                .mapValues(textLine -> textLine.toLowerCase())
//        3. FlatMapValue to split the message by space
                .flatMapValues(textLine -> Arrays.asList(textLine.split("\\W+")))
//        4. SelectKey change the Key name similar to value
                .selectKey((key, word) -> word)
//        5. GroupKey group the key to count the words
                .groupByKey()
//        6. Count
                .count(Materialized.as("Counts"));
//        7. TO Save the msg to back to kafka under target topic


        wordCounts.toStream().print(Printed.toSysOut());

//        wordCounts.toStream().foreach((key,value)->{
//            System.out.println("Key: "+key+ ", Value: "+value);
//        });

        wordCounts.toStream().to("word-count-outpu", Produced.with(Serdes.String(),Serdes.Long()));

        return streamsBuilder.build();

    }
    public static void main( String[] args )
    {
        System.out.println( "Hello Kafka Stream!" );

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"wordcount-application");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.144.101:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

        StreamsStarterApp streamsStarterApp= new StreamsStarterApp();

        KafkaStreams streams=new KafkaStreams(streamsStarterApp.createTopology(),properties);
        streams.start();
        //  Printed topologies
        System.out.println("Printed topologies: "+streams.toString());

//      Shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));


    }
}
