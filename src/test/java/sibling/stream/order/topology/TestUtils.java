package sibling.stream.order.topology;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.kafka.streams.test.TestRecord;

import sibling.stream.order.model.Order;
import sibling.stream.order.model.OrderPayment;
import sibling.stream.order.model.OrderPayment.OrderPaymentType;
import sibling.stream.order.model.OrderTracking;
import sibling.stream.order.model.OrderTracking.OrderTrackingState;

public class TestUtils {
	
	public static TestRecord<String, Order> ordersValidate(String orderId) {
		var order1 = Order.builder().id(orderId).orderDate(new Date()).amount(new BigDecimal(500))
				.trackings(orderTrackingRequested(orderId)).payments(orderPayment(orderId)).build();

		return new TestRecord(order1.getId().toString(), order1);
	}
	public static TestRecord<String, Order> ordersFraud(String orderId) {
		var order1 = Order.builder().id(orderId).orderDate(new Date()).amount(new BigDecimal(500))
				.trackings(orderTrackingValidated(orderId)).payments(orderPayment(orderId)).build();

		return new TestRecord(order1.getId().toString(), order1);
	}
	public static TestRecord<String, Order> ordersPayment(String orderId) {
		var order1 = Order.builder().id(orderId).orderDate(new Date()).amount(new BigDecimal(500))
				.trackings(orderTrackingFraud(orderId)).payments(orderPayment(orderId)).build();

		return new TestRecord(order1.getId().toString(), order1);
	}
	public static TestRecord<String, Order> ordersPrepare(String orderId) {
		var order1 = Order.builder().id(orderId).orderDate(new Date()).amount(new BigDecimal(500))
				.trackings(orderTrackingPayment(orderId)).payments(orderPayment(orderId)).build();

		return new TestRecord(order1.getId().toString(), order1);
	}
	public static TestRecord<String, Order> ordersTransport(String orderId) {
		var order1 = Order.builder().id(orderId).orderDate(new Date()).amount(new BigDecimal(500))
				.trackings(orderTrackingPrepared(orderId)).payments(orderPayment(orderId)).build();

		return new TestRecord(order1.getId().toString(), order1);
	}
	public static TestRecord<String, Order> ordersDelivery(String orderId) {
		var order1 = Order.builder().id(orderId).orderDate(new Date()).amount(new BigDecimal(500))
				.trackings(orderTrackingTransport(orderId)).payments(orderPayment(orderId)).build();

		return new TestRecord(order1.getId().toString(), order1);
	}
	
	public static List<OrderTracking> orderTrackingRequested(String orderId) {
		List<OrderTracking> orderTrackingList = List.of(
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.REQUESTED).build());
		return orderTrackingList;
	}
	public static List<OrderTracking> orderTrackingValidated(String orderId) {
		List<OrderTracking> orderTrackingList = List.of(
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.REQUESTED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.VALIDATED).build()
				);
		return orderTrackingList;
	}
	public static List<OrderTracking> orderTrackingFraud(String orderId) {
		List<OrderTracking> orderTrackingList = List.of(
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.REQUESTED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.VALIDATED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.LOW_FRAUD).build()
				);
		return orderTrackingList;
	}
	public static List<OrderTracking> orderTrackingPayment(String orderId) {
		List<OrderTracking> orderTrackingList = List.of(
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.REQUESTED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.VALIDATED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.LOW_FRAUD).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.APPROVED).build()
				);
		return orderTrackingList;
	}
	public static List<OrderTracking> orderTrackingPrepared(String orderId) {
		List<OrderTracking> orderTrackingList = List.of(
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.REQUESTED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.VALIDATED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.LOW_FRAUD).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.APPROVED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.SHIPMENT_PREPARED).build()
				);
		return orderTrackingList;
	}
	public static List<OrderTracking> orderTrackingTransport(String orderId) {
		List<OrderTracking> orderTrackingList = List.of(
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.REQUESTED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.VALIDATED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.LOW_FRAUD).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.APPROVED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.SHIPMENT_PREPARED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.SHIPMENT_DISPATCHED).build()
				);
		return orderTrackingList;
	}
	public static List<OrderTracking> orderTrackingDelivery(String orderId) {
		List<OrderTracking> orderTrackingList = List.of(
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.REQUESTED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.VALIDATED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.LOW_FRAUD).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.APPROVED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.SHIPMENT_PREPARED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.SHIPMENT_DISPATCHED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.SHIPMENT_DELIVERED).build()
				);
		return orderTrackingList;
	}
	public static List<OrderTracking> orderTrackingCompleted(String orderId) {
		List<OrderTracking> orderTrackingList = List.of(
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.REQUESTED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.VALIDATED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.LOW_FRAUD).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.APPROVED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.SHIPMENT_PREPARED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.SHIPMENT_DISPATCHED).build(),
				OrderTracking.builder().orderId(orderId).time(new Date()).state(OrderTrackingState.SHIPMENT_DELIVERED).build()
				);
		return orderTrackingList;
	}
	
	public static List<OrderPayment> orderPayment(String orderId) {
		List<OrderPayment> orderPaymentList = List.of(
				OrderPayment.builder().orderId(orderId).time(new Date()).type(OrderPaymentType.CREDIT_CARD)
						.amount(new BigDecimal(250)).build(),
				OrderPayment.builder().orderId(orderId).time(new Date()).type(OrderPaymentType.CRYPTO)
						.amount(new BigDecimal(250)).build());
		return orderPaymentList;
	}
	

	public static boolean checkTrackingInList(List<OrderTracking> orderTrackingList, OrderTrackingState trackingState) {
		for (OrderTracking orderTracking : orderTrackingList) {
			if (trackingState.name().equals(orderTracking.getState().name())) {
				return true;
			}
		}
		return false;
	}
}
