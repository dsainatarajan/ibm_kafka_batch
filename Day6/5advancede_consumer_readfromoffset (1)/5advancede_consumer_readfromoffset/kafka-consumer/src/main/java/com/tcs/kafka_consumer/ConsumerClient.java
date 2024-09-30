package com.tcs.kafka_consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerClient {
	
	public static void main(String[] args) 
	{
		  String topicName = "first_topic";
		
	      //Creating the consumer properties
	      Properties properties = new Properties();
	      
	      properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	      properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	      properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	      properties.put(ConsumerConfig.GROUP_ID_CONFIG, "TestConsumerGroup");
	      properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
	      properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
	      properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"100");
	      
	      //Creating the consumer
	      KafkaConsumer<String,String> consumer= new KafkaConsumer<String,String>(properties);  
	      
		TopicPartition partitionToReadFrom = new TopicPartition(topicName, 0);
        long offsetToReadFrom = 7L;
        consumer.assign(Arrays.asList(partitionToReadFrom));

        // seek
        consumer.seek(partitionToReadFrom, offsetToReadFrom);

        int numberOfMessagesToRead = 5;
        boolean keepOnReading = true;
        int numberOfMessagesReadSoFar = 0;
	      
	      //print the topic name
	      System.out.println("Subscribed to the topic " + topicName);
	      
	    //polling
          while(keepOnReading) 
          {
        	  ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100)); 
        	  for (ConsumerRecord<String, String> record : records)
        	  {
        	         System.out.printf("offset = %d, key = %s, value = %s\n", 
        	                 record.offset(), record.key(), record.value());
					 numberOfMessagesReadSoFar += 1;
					 consumer.commitSync(Collections.singletonMap(partitionToReadFrom, 
							  new OffsetAndMetadata(record.offset())));
					  if (numberOfMessagesReadSoFar >= numberOfMessagesToRead){
						keepOnReading = false; // to exit the while loop
						break; // to exit the for loop
					}
        	  }
			  
			  // consumer.commitSync();
          }	           
	}
}
