Hello,
This is a Kafka Streams word count demo application.
Confluent Kafka Streams version - 0.11.0.0.

All required properties files are available under the etc/ directory.

**Start Zookeeper using Nohupe command**

nohup zookeeper-server-start ~/kafka/confluent-7.4.1/etc/kafka/zookeeper.properties &

**Start Broker using Nohupe command**

nohup kafka-server-start ~/kafka/confluent-7.4.1/etc/kafka/server.properties >> broker.log &

**Consumer**

kafka-console-consumer --bootstrap-server localhost:9092 --topic word-count-output --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property print.value=trye --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

**Producer**
kafka-console-producer --bootstrap-server localhost:9092 --topic word-count-input

You can type the sentence in upper case. SteamStarterApp convert the message in lower case 
and perform the process of collecting and summarize data to gain insights 

