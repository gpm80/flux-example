package ru.gpm.example.flux.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.gpm.example.flux.model.User;
import ru.gpm.example.flux.model.Order;
import ru.gpm.example.flux.model.UserInfoOrders;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveService {

    private final WebClient webClient;

    public UserInfoOrders getRequestsInfoUser(int id) {
        log.info(">>> request userId: {}", id);
        UserInfoOrders userTotal = new UserInfoOrders();
        return Flux.merge(
                webClient.get()
                    .uri("/user?id=" + id).retrieve()
                    .bodyToMono(User.class)
                    .retryWhen(Retry.fixedDelay(2, Duration.ofMillis(100)))
                    .map(userInfo -> process(userTotal, userInfo)),
                webClient.get()
                    .uri("/orders/byUser?userId=" + id)
                    .retrieve()
                    .bodyToFlux(Order.class)
                    .map(userOrder -> process(userTotal, userOrder)))
            .blockLast();
    }

    private UserInfoOrders process(UserInfoOrders userInfoOrders, User user) {
        log.debug("<<< returned userId: {}", user.getId());
        return userInfoOrders.setUser(user);
    }

    private UserInfoOrders process(UserInfoOrders userInfoOrders, Order order) {
        log.debug("returned order {}", order);
        userInfoOrders.getOrders().add(order);
        return userInfoOrders;
    }
}
