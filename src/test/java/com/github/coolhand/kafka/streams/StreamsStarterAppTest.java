package com.github.coolhand.kafka.streams;


import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class StreamsStarterAppTest {

    TopologyTestDriver testDriver;

    StreamsBuilder streamsBuilder = new StreamsBuilder();







    @Before
    public void setUpTopologyTestDriver(){

        Properties config=new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsStarterApp streamsStarterApp = new StreamsStarterApp();
        Topology topology = streamsStarterApp.createTopology();

        testDriver=new TopologyTestDriver(topology,config);

    }

    @After
    public void tearDown(){
        testDriver.close();
    }

    public void pushNewInputRecord(String value){
        StringSerializer stringSerializer = new StringSerializer();
        TestInputTopic<String, String> inputTopic=testDriver.createInputTopic("wordcount-stream-input", stringSerializer, stringSerializer);
        inputTopic.pipeInput(value);
    }

    public TestOutputTopic<String, Long> readOutput(){
        return testDriver.createOutputTopic("wordcount-stream-output", new StringDeserializer(), new LongDeserializer());
    }

    @Test
    public void TestFirstInput(){
        pushNewInputRecord("testing Kafka Streams");
        readOutput();

        assertThat(readOutput().readKeyValue().key, equalTo("TESTING"));
        assertThat(readOutput().readKeyValue().key, equalTo("KAFKA"));
        assertThat(readOutput().readKeyValue().key, equalTo("STREAMS"));
//        assertThat(readOutput().readKeyValue().key, equalTo(new KeyValue<>("STREAMS",1)));


    }

}
