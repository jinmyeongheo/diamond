package jm.diamond.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Async("approvalTaskExecutor")
    public void sendEmail() {

        CompletableFuture<String> supplyFuture = CompletableFuture.supplyAsync(this::callEmailSever);
        String result = supplyFuture
                .thenApply(email -> email + "\n") // 반환형 있음.
                .join(); // blocking


        supplyFuture.thenAccept(s -> log.info("gd")); // 반환없음.

        CompletableFuture<Void> runFuture = CompletableFuture.runAsync(this::callEmailSever);
        Void join1 = runFuture.join();


        String message = "Hello";
        CompletableFuture<String> completedFuture = CompletableFuture.completedFuture(message);
        String join = completedFuture.join();

    }

    public String callEmailSever(){
        log.info("callEmailSever");

        return "callEmailSever";
    }


}
