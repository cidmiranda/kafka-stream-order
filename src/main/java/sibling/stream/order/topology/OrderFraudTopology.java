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
import org.springframework.stereotype.Component;

import sibling.stream.order.model.JsonSerde;
import sibling.stream.order.model.Order;
import sibling.stream.order.model.OrderTracking;
import sibling.stream.order.model.OrderTracking.OrderTrackingState;
import sibling.stream.order.utils.Utils;

@Component
public class OrderFraudTopology {

	@Autowired
	public void process(StreamsBuilder streamsBuilder) {
		Serde<Order> orderSerde = new JsonSerde<>(Order.class);

		KStream<String, Order> orderStreams = streamsBuilder
				.stream(Utils.TOPIC_FRAUDS, Consumed.with(Serdes.String(), new JsonSerde<>(Order.class)))
				.map((key, order) -> new KeyValue<>(order.getId(), processFraud(order)));

		orderStreams.print(Printed.<String, Order>toSysOut().withLabel("orderFraud"));
		orderStreams.to(Utils.TOPIC_PAYMENTS, Produced.with(Serdes.String(), orderSerde));
	}

	public static Order processFraud(Order order) {
		OrderTracking orderTracking = OrderTracking.builder().orderId(order.getId()).time(new Date())
				.state(OrderTrackingState.LOW_FRAUD).build();
		order.getTrackings().add(orderTracking);
		return order;
	}

}