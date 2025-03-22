package sibling.stream.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonSerde;

import sibling.stream.order.model.Order;
import sibling.stream.order.model.OrderPayment;
import sibling.stream.order.model.OrderPayment.OrderPaymentType;
import sibling.stream.order.model.OrderTracking;
import sibling.stream.order.model.OrderTracking.OrderTrackingState;
import sibling.stream.order.topology.OrderValidateTopology;

class OrdersValidateTopologyTest {

	TopologyTestDriver topologyTestDriver;
	TestInputTopic<String, Order> ordersInputTopic;
	TestOutputTopic<String, Order> ordersOutputTopic;
	StreamsBuilder streamsBuilder;
	OrderValidateTopology ordersValidateTopology = new OrderValidateTopology();

	public static final String ORDER_ID = "a4ae8503e1ba1f53a063d81e407ba636";

	@BeforeEach
	void setUp() {
		JsonSerde<Order> jsonSerde = new JsonSerde<Order>(Order.class);
		final StreamsBuilder builder = new StreamsBuilder();
		// Create Actual Stream Processing pipeline
		ordersValidateTopology.process(builder);
		topologyTestDriver = new TopologyTestDriver(builder.build());
		ordersInputTopic = topologyTestDriver.createInputTopic(OrderValidateTopology.INPUT_TOPIC, Serdes.String().serializer(),
				jsonSerde.serializer());
		ordersOutputTopic = topologyTestDriver.createOutputTopic(OrderValidateTopology.OUTPUT_TOPIC, Serdes.String().deserializer(),
				jsonSerde.deserializer());
	}

	@AfterEach
	void tearDown() {
		topologyTestDriver.close();
	}

	@Test
	void ordersValidateId() {
		ordersInputTopic.pipeInput(orders());
		assertThat(ordersOutputTopic.readValue().getId()).isEqualTo(ORDER_ID);
		//No more output in topic
	    assertThat(ordersOutputTopic.isEmpty()).isTrue();
	    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
	    	ordersOutputTopic.readValue();
	    }).withMessage("Empty topic: %s", ordersValidateTopology.OUTPUT_TOPIC);
	}
	@Test
	void ordersValidateTracking() {
		ordersInputTopic.pipeInput(orders());
		assertThat(ordersOutputTopic.readValue().getTrackings().size()).isEqualTo(2);
	}

	static TestRecord<String, Order> orders() {
		List<OrderPayment> orderPaymentList = List.of(
				OrderPayment.builder().orderId(ORDER_ID).time(new Date())
						.type(OrderPaymentType.CREDIT_CARD).amount(new BigDecimal(250)).build(),
				OrderPayment.builder().orderId(ORDER_ID).time(new Date())
						.type(OrderPaymentType.CRYPTO).amount(new BigDecimal(250)).build());
		List<OrderTracking> orderTrackingList = List
				.of(OrderTracking.builder().orderId(ORDER_ID).time(new Date())
						.state(OrderTrackingState.REQUESTED).build());

		Order.builder().id(ORDER_ID).orderDate(new Date()).amount(new BigDecimal(500))
				.trackings(orderTrackingList).payments(orderPaymentList).build();

		var order1 = Order.builder().id(ORDER_ID).orderDate(new Date())
				.amount(new BigDecimal(500)).trackings(orderTrackingList).payments(orderPaymentList).build();
		
		return new TestRecord(order1.getId().toString(), order1);
	}
}