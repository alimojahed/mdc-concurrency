package dev.alimojahed.mdc.springdemo.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Ali Mojahed on 9/21/2022
 * @project mdcconcurrency
 **/

@Service
@Log4j2
public class NonMdcService {

    public void stream(int numberOfJobs) {
        List<Integer> test = IntStream.range(0, numberOfJobs).boxed().collect(Collectors.toList());

        test.parallelStream().forEach(integer -> {
            try {
                Thread.sleep(5);
                log.info("job {} is done ", integer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }


    public void executor(int numberOfJobs) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfJobs);
        List<Future> futures = new ArrayList<>();
        IntStream.range(0, numberOfJobs)
                .boxed()
                .forEach(integer -> {
                    executorService.execute(() -> {
                        try {
                            Thread.sleep(5);
                            log.info("job {} is done ", integer);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                });

        executorService.awaitTermination(1, TimeUnit.SECONDS);


    }

    public void completableFuture(int numberOfJobs) {
        List<CompletableFuture<Void>> jobs = IntStream.range(0, numberOfJobs)
                .boxed()
                .map(integer ->  CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(5);
                        log.info("job {} is done ", integer);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }))
                .collect(Collectors.toList());


        CompletableFuture<Void> allJobs = CompletableFuture.allOf(jobs.toArray(new CompletableFuture[0]));

        try {
            allJobs.thenApply(future -> jobs.stream().map(CompletableFuture::join)).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }


}
