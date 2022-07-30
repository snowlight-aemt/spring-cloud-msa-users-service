package me.snowlight.springcloudusers.model.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import me.snowlight.springcloudusers.controller.ResponseOrder;

@FeignClient(name = "order-service")
public interface OrderServiceClient {
    
    @GetMapping("/{userId}/orders")
    List<ResponseOrder> getOrders(@PathVariable String userId);
}
