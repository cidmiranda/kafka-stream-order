package sibling.stream.order.topology;

import java.util.Date;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import sibling.stream.order.model.JsonSerde;
import sibling.stream.order.model.Order;
import sibling.stream.order.model.OrderTracking;
import sibling.stream.order.model.OrderTracking.OrderTrackingState;

@Component
public class OrderValidateTopology {

	//@Value("${topics.orders}")
	//private String topicOrders;

	//@Value("${topics.frauds}")
	//private String topicFrauds;
	
	public static final String INPUT_TOPIC = "orders_";
    public static final String OUTPUT_TOPIC = "frauds_";

	@Autowired
	public void process(StreamsBuilder streamsBuilder) {
		Serde<Order> orderSerde = new JsonSerde<>(Order.class);

		KStream<String, Order> orderStreams = streamsBuilder
				.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), new JsonSerde<>(Order.class)))
				.filter((key, value) -> value.getTrackings().get(0).state == OrderTracking.OrderTrackingState.REQUESTED)
				.map((key, order) -> new KeyValue<>(order.getId(), validateOrder(order)));

		orderStreams.print(Printed.<String, Order>toSysOut().withLabel("orderValidate"));
		orderStreams.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), orderSerde));
	}

	public static Order validateOrder(Order order) {
		OrderTracking orderTracking = OrderTracking.builder().orderId(order.getId()).time(new Date())
				.state(OrderTrackingState.VALIDATED).build();
		order.getTrackings().add(orderTracking);
		return order;
	}

}