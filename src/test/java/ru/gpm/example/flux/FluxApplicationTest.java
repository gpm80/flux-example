package ru.gpm.example.flux;

import static java.lang.Thread.sleep;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.gpm.example.flux.model.UserInfoOrders;
import ru.gpm.example.flux.service.ReactiveService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FluxApplicationTest {

    private final static int THREADS = 3;
    private final static int REQUESTS = 30;
    @Autowired
    private ReactiveService reactiveService;

    @Test
    void testMergeProcesses() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
        final List<UserInfoOrders> resultList = new CopyOnWriteArrayList<>();
        List<CompletableFuture<UserInfoOrders>> tasks = new ArrayList<>();
        IntStream.range(0, REQUESTS).forEach(value ->
            tasks.add(CompletableFuture
                .supplyAsync(() -> reactiveService.getRequestsInfoUser(value), executorService)
                .thenApply(info -> {
                    resultList.add(info);
                    log.info("apply userId: {}", info.getUser().getId());
                    return info;
                }))
        );
        boolean work;
        do {
            log.trace("checking...");
            work = tasks.stream().anyMatch(f -> !f.isDone());
            sleep(100);
        } while (work);
        Assertions.assertEquals(REQUESTS, resultList.size());
    }
}