package ru.gpm.example.flux.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gpm.example.flux.model.Order;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping("/orders")
public class UserOrderController {

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    @GetMapping("/byUser")
    List<Order> getByUser(@RequestParam("userId") Integer userId) throws InterruptedException {
        log.debug("request orders by user id={}", userId);
        Thread.sleep(random.nextLong(1000));
        return IntStream.range(3, random.nextInt(7) + 4)
            .mapToObj(i -> new Order()
                .setNumber(random.nextInt(1000))
                .setQty(random.nextInt(10) + 1)
                .setSku(UUID.randomUUID().toString().substring(0, 5) + "-" + userId))
            .toList();
    }
}
