This is a Kafka Streams word count demo application.  
Confluent Kafka Streams version - 0.11.0.0.  

All required properties files are available under the etc/ directory.  

**Start Zookeeper using Nohupe command**  
nohup zookeeper-server-start ~/kafka/confluent-7.4.1/etc/kafka/zookeeper.properties &

**Start Broker using Nohupe command**  
nohup kafka-server-start ~/kafka/confluent-7.4.1/etc/kafka/server.properties >> broker.log &

**Create two topic 1. input and 2. output for stream**  
_kafka-topics --bootstrap-server localhost:9092 --partitions 2 --create --topic word-count-input
kafka-topics --bootstrap-server localhost:9092 --partitions 2 --create --topic word-count-output_

**Consumer**  
kafka-console-consumer --bootstrap-server localhost:9092 --topic word-count-output --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property print.value=trye --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

**Producer**  
_kafka-console-producer --bootstrap-server localhost:9092 --topic word-count-input_  
You can type the sentence in upper case. SteamStarterApp convert the message in lower case 
and perform the process of collecting and summarize data to gain insights 

**Fat_Jar(Package)** _Packaging the Wordcount application as fat jar_  
    1. Modify plugin block in pom.xml add new plugin which contain manifest section along with main class path  
    2. Go to command line and run mvn clean package or you can use editor (eclipse/intelji) to make package  
    3. Once package is ready please run below command from terminal   
            java -jar .\target\wordcount-1.0-SNAPSHOT-jar-with-dependencies.jar

**Log Compactions**  
properties 
    --config cleanup.policy=compact (new topic will be compacted topice)
    --config min.cleanable.dirty.ration=0.005 (**Default is 0.5**)
    --config segment.ms=10000 (**every 10 sec kafka will allow new segement** )

**create topic with log compactions parameters**  
kafka-topics --bootstrap-server localhost:9092 --create
--topic employee-salary-compact
--partitions 1 
--replication-factor 1
--config cleanup.policy=compact
--config min.cleanable.dirty.ration=0.005 (Default is 0.5)
--config segment.ms=10000

**Print data on log/console from consumer**  
kafka-console-consumer --bootstrap-server localhost:9092
--topic employee-salary-compact
--from-beginning
--property print.key=true
--property key.separator=,

**Produce message**
kafka-console-producer --broker-list localhost:9092
--topic employee-salary-compact
--property parse.key=true
--property key.separator=,
