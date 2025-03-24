package sibling.stream.order.topology;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonSerde;

import sibling.stream.order.model.Order;
import sibling.stream.order.model.OrderTracking;
import sibling.stream.order.model.OrderTracking.OrderTrackingState;
import sibling.stream.order.utils.Utils;

class OrdersTransportTopologyTest {

	TopologyTestDriver topologyTestDriver;
	TestInputTopic<String, Order> ordersInputTopic;
	TestOutputTopic<String, Order> ordersOutputTopic;
	StreamsBuilder streamsBuilder;
	OrderTransportTopology ordersTransportTopology = new OrderTransportTopology();

	public static final String ORDER_ID = "a4ae8503e1ba1f53a063d81e407ba636";

	@BeforeEach
	void setUp() {
		JsonSerde<Order> jsonSerde = new JsonSerde<Order>(Order.class);
		final StreamsBuilder builder = new StreamsBuilder();
		// Create Actual Stream Processing pipeline
		ordersTransportTopology.process(builder);
		topologyTestDriver = new TopologyTestDriver(builder.build());
		ordersInputTopic = topologyTestDriver.createInputTopic(Utils.TOPIC_TRANSPORTS,
				Serdes.String().serializer(), jsonSerde.serializer());
		ordersOutputTopic = topologyTestDriver.createOutputTopic(Utils.TOPIC_DELIVERIES,
				Serdes.String().deserializer(), jsonSerde.deserializer());
	}

	@AfterEach
	void tearDown() {
		topologyTestDriver.close();
	}

	@Test
	void ordersTransportId() {
		ordersInputTopic.pipeInput(TestUtils.ordersTransport(ORDER_ID));
		// Veirfy orderId
		assertThat(ordersOutputTopic.readValue().getId()).isEqualTo(ORDER_ID);
		// No more output in topic
		assertThat(ordersOutputTopic.isEmpty()).isTrue();
	}

	@Test
	void ordersTransportTracking() {
		ordersInputTopic.pipeInput(TestUtils.ordersTransport(ORDER_ID));
		List<OrderTracking> listTracking = ordersOutputTopic.readValue().getTrackings();
		// Verify if tracking list has 6 trackings
		assertThat(listTracking.size()).isEqualTo(6);
		assertThat(TestUtils.checkTrackingInList(listTracking, OrderTrackingState.REQUESTED)).isTrue();
		assertThat(TestUtils.checkTrackingInList(listTracking, OrderTrackingState.VALIDATED)).isTrue();
		assertThat(TestUtils.checkTrackingInList(listTracking, OrderTrackingState.LOW_FRAUD)).isTrue();
		assertThat(TestUtils.checkTrackingInList(listTracking, OrderTrackingState.APPROVED)).isTrue();
		assertThat(TestUtils.checkTrackingInList(listTracking, OrderTrackingState.SHIPMENT_PREPARED)).isTrue();
		assertThat(TestUtils.checkTrackingInList(listTracking, OrderTrackingState.SHIPMENT_DISPATCHED)).isTrue();
	}

}