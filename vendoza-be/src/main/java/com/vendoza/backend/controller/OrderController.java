package com.vendoza.backend.controller;

import com.vendoza.backend.entity.Order;
import com.vendoza.backend.entity.OrderItem;
import com.vendoza.backend.repository.OrderRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public Order createOrder(@RequestBody String body) {
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        Order order = new Order();
        order.setOrderId(json.get("orderId").getAsString());
        order.setUserId(json.get("userId").getAsLong());
        order.setSubtotal(json.get("subtotal").getAsDouble());
        order.setShippingCost(json.get("shippingCost").getAsDouble());
        order.setTotal(json.get("totalAmount").getAsDouble());
        order.setShippingAddress(json.get("shippingAddress").getAsString());
        order.setPaymentMethod(json.get("paymentMethod").getAsString());
        order.setShippingMethod(json.get("shippingMethod").getAsString());
        order.setStatus("Pending");
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();
        JsonArray itemsArray = json.getAsJsonArray("items");
        for (int i = 0; i < itemsArray.size(); i++) {
            JsonObject itemJson = itemsArray.get(i).getAsJsonObject();
            OrderItem item = new OrderItem();
            item.setProductId(itemJson.get("productId").getAsLong());
            item.setProductName(itemJson.get("productName").getAsString());
            item.setQuantity(itemJson.get("quantity").getAsInt());
            item.setPrice(itemJson.get("price").getAsDouble());
            item.setOrder(order);
            items.add(item);
        }
        order.setItems(items);

        return orderRepository.save(order);
    }

    @GetMapping("/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @PutMapping("/{orderId}/status")
    public Order updateStatus(@PathVariable Long orderId,
                                    @RequestParam String status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        return orderRepository.save(order);
    }
}