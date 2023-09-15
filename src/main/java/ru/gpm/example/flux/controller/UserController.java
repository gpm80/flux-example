package ru.gpm.example.flux.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gpm.example.flux.model.User;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    @GetMapping()
    User getOne(@RequestParam("id") Integer id) throws InterruptedException {
        log.debug("request user by id: {}", id);
        Thread.sleep(random.nextLong(1000));
        randomException();
        return new User().setId(id)
            .setName("user-" + id)
            .setAge(new Random().nextInt(50) + 18);
    }

    private void randomException() {
        if (random.nextInt(10) > 8) {
            throw new RuntimeException("random exception");
        }
    }
}
