package sibling.stream.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafkaStreams
public class CustomStreamsConfig {
	
	@Value("${topics.orders}")
	private String topicOrders;
	
	@Value("${topics.frauds}")
	private String topicFrauds;
	
	@Value("${topics.payments}")
	private String topicPayments;
	
	@Value("${topics.shipments}")
	private String topicShipments;
	
	@Value("${topics.transports}")
	private String topicTransports;

	@Value("${topics.deliveries}")
	private String topicDeliveries;
	
	@Value("${topics.completed}")
	private String topicCompleted;
	
	@Value("${topics.partitions}")
	private Integer partitions;
	
	@Value("${topics.replicas}")
	private Integer replicas;
	
	@Bean
	  public NewTopic order() {
	    return TopicBuilder.name(topicOrders)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
	
	@Bean
	  public NewTopic fraud() {
	    return TopicBuilder.name(topicFrauds)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
	
	@Bean
	  public NewTopic shippment() {
	    return TopicBuilder.name(topicShipments)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
	
	@Bean
	  public NewTopic payment() {
	    return TopicBuilder.name(topicPayments)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
	
	@Bean
	  public NewTopic transport() {
	    return TopicBuilder.name(topicTransports)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
	
	@Bean
	  public NewTopic delivery() {
	    return TopicBuilder.name(topicDeliveries)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
	
	@Bean
	  public NewTopic completed() {
	    return TopicBuilder.name(topicCompleted)
	        .partitions(partitions)
	        .replicas(replicas)
	        .build();
	  }
}
