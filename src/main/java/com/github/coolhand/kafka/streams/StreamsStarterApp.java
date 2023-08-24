package com.github.coolhand.kafka.streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.Arrays;
import java.util.Properties;

/**
 * Hello Kafka Stream!
 *
 */
public class StreamsStarterApp
{
    public static void main( String[] args )
    {
        System.out.println( "Hello Kafka Stream!" );

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"wordcount");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

//        1. Stream
        KStreamBuilder builder = new KStreamBuilder();
        KStream<Object, Object> wordCountInput = builder.stream("word-couont-input");

//        2. Map the key & value in lowercase
        KTable<String, Long> wordCounts = wordCountInput.mapValues(value -> value.toString().toLowerCase())
//        3. FlatMapValue to split the message by space
                .flatMapValues(value -> Arrays.asList(value.split(" ")))
//        4. SelectKey change the Key name similar to value
                .selectKey((ignoreKey, word) -> word)
//        5. GroupKey group the key to count the words
                .groupByKey()
//        6. Count
                .count("Counts");

//        7. TO Save the msg to back to kafka under target topic
        wordCounts.to(Serdes.String(),Serdes.Long(),"word-count-output");

        KafkaStreams streams=new KafkaStreams(builder,properties);
        streams.start();
        //  Print topologies
        System.out.println(streams.toString());

//      Shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));









    }
}
