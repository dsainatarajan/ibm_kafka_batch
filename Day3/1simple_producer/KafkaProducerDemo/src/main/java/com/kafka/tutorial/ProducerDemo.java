package com.kafka.tutorial;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;


public class ProducerDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

        String bootstrapServers = "kafka1:9092,kafka2:9092,kafka3:9092";

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        // create a producer record
        ProducerRecord<String, String> record =
                new ProducerRecord<String, String>("first_topic", "hello world");
		// public ProducerRecord(String topic, V value);
		// public ProducerRecord(String topic, K key, V value);
		//ProducerRecord<String, String> record =
        //        new ProducerRecord<String, String>("first_topic", "greeting", "hello world");
				
        // send data - asynchronous
        producer.send(record);
		
		ProducerRecord<String, String> record2 =
                new ProducerRecord<String, String>("first_topic", "greeting", "hello IBM");
		producer.send(record2);
        // flush data
        producer.flush();
        // flush and close producer
        producer.close();

	}

}
