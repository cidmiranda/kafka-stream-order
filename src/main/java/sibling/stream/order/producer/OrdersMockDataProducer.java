package sibling.stream.order.producer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import sibling.stream.order.model.Order;
import sibling.stream.order.model.OrderPayment;
import sibling.stream.order.model.OrderPayment.OrderPaymentType;
import sibling.stream.order.model.OrderTracking;
import sibling.stream.order.model.OrderTracking.OrderTrackingState;

@Slf4j
public class OrdersMockDataProducer {

public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
  public static void main(String[] args) throws InterruptedException {
    publishOrders(buildOrders());
  }

  private static List<Order> buildOrders() {

	List<OrderTracking> orderTrackingList = List.of(
	  OrderTracking.builder()
      .orderId("a4ae8503e1ba1f53a063d81e407ba636")
      .time(new Date())
      .state(OrderTrackingState.REQUESTED)
      .build()
	);
	
	List<OrderPayment> orderPaymentList = List.of(
			  OrderPayment.builder()
		      .orderId("a4ae8503e1ba1f53a063d81e407ba636")
		      .time(new Date())
		      .type(OrderPaymentType.CREDIT_CARD)
		      .amount(new BigDecimal(250))
		      .build(),
		      OrderPayment.builder()
		      .orderId("a4ae8503e1ba1f53a063d81e407ba636")
		      .time(new Date())
		      .type(OrderPaymentType.CRYPTO)
		      .amount(new BigDecimal(250))
		      .build()
			);
	
	List<Order> data = List.of(
			Order.builder()
		      .id("a4ae8503e1ba1f53a063d81e407ba636")
		      .orderDate(new Date())
		      .amount(new BigDecimal(500))
		      .trackings(orderTrackingList)
		      .payments(orderPaymentList)
		      .build()
	);
	
    return data;
  }

  private static void publishOrders(List<Order> orders) {
	  KafkaProducer<String, String> orderProducer =
              new KafkaProducer<>(Map.of(
                      ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
              ));
	  
	  orders.stream()
      .map(order -> new ProducerRecord<>("orders_", order.getId(), toJson(order)))
      .forEach(record -> send(orderProducer, record));
  }

  @SneakyThrows
  private static void send(KafkaProducer<String, String> orderProducer, ProducerRecord<String, String> record) {
	  orderProducer.send(record).get();
  }
  
  @SneakyThrows
  private static String toJson(Order order) {
      return OBJECT_MAPPER.writeValueAsString(order);
  }
}