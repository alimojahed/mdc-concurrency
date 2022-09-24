package dev.alimojahed.mdc.springdemo.controller;

import dev.alimojahed.mdc.springdemo.service.MdcService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @author Ali Mojahed on 9/21/2022
 * @project mdcconcurrency
 **/

@RestController
public class MdcController {

    private final MdcService MdcService;

    public MdcController(MdcService MdcService) {
        this.MdcService = MdcService;
    }

    @GetMapping("/mdc/executor")
    public void executor(@RequestParam(name = "numberOfJobs") int numberOfJobs) throws InterruptedException {
        MdcService.executor(numberOfJobs);
    }

    @GetMapping("/mdc/future")
    public void completableFuture(@RequestParam(name = "numberOfJobs") int numberOfJobs) throws ExecutionException, InterruptedException {
        MdcService.completableFuture(numberOfJobs);
    }

    @GetMapping("/mdc/stream")
    public void stream(@RequestParam(name = "numberOfJobs") int numberOfJobs) {
        MdcService.stream(numberOfJobs);
    }
}
