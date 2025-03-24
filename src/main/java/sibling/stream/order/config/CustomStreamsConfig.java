package sibling.stream.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;

import sibling.stream.order.utils.Utils;

@Configuration
@EnableKafkaStreams
public class CustomStreamsConfig {
		
	@Value("${topics.partitions}")
	private Integer partitions;
	
	@Value("${topics.replicas}")
	private Integer replicas;
	
	@Bean
	  public NewTopic order() {
	    return TopicBuilder.name(Utils.TOPIC_ORDERS)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
	
	@Bean
	  public NewTopic fraud() {
	    return TopicBuilder.name(Utils.TOPIC_FRAUDS)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
	
	@Bean
	  public NewTopic shippment() {
	    return TopicBuilder.name(Utils.TOPIC_SHIPMENTS)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
	
	@Bean
	  public NewTopic payment() {
	    return TopicBuilder.name(Utils.TOPIC_PAYMENTS)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
	
	@Bean
	  public NewTopic transport() {
	    return TopicBuilder.name(Utils.TOPIC_TRANSPORTS)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
	
	@Bean
	  public NewTopic delivery() {
	    return TopicBuilder.name(Utils.TOPIC_DELIVERIES)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
	
	@Bean
	  public NewTopic completed() {
	    return TopicBuilder.name(Utils.TOPIC_COMPLETED)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
}
