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
public class OrderTransportTopology {

	@Value("${topics.transports}")
	private String topicTransports;

	@Value("${topics.deliveries}")
	private String topicDeliveries;

	@Autowired
	public void process(StreamsBuilder streamsBuilder) {
		Serde<Order> orderSerde = new JsonSerde<>(Order.class);

		KStream<String, Order> orderStreams = streamsBuilder
				.stream(topicTransports, Consumed.with(Serdes.String(), new JsonSerde<>(Order.class)))
				.map((key, order) -> new KeyValue<>(order.getId(), processTransport(order)));

		orderStreams.print(Printed.<String, Order>toSysOut().withLabel("orderTransport"));
		orderStreams.to(topicDeliveries, Produced.with(Serdes.String(), orderSerde));
	}

	public static Order processTransport(Order order) {
		OrderTracking orderTracking = OrderTracking.builder().orderId(order.getId()).time(new Date())
				.state(OrderTrackingState.SHIPMENT_DISPATCHED).build();
		order.getTrackings().add(orderTracking);
		return order;
	}

}