package dev.alimojahed.mdc.springdemo.controller;

import dev.alimojahed.mdc.springdemo.service.NonMdcService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ali Mojahed on 9/21/2022
 * @project mdcconcurrency
 **/

@RestController
public class NonMdcController {
    private final NonMdcService nonMdcService;

    public NonMdcController(NonMdcService nonMdcService) {
        this.nonMdcService = nonMdcService;
    }

    @GetMapping("/non/executor")
    public void executor(@RequestParam(name = "numberOfJobs")int numberOfJobs) throws InterruptedException {
        nonMdcService.executor(numberOfJobs);
    }

    @GetMapping("/non/future")
    public void completableFuture(@RequestParam(name = "numberOfJobs")int numberOfJobs) {
        nonMdcService.completableFuture(numberOfJobs);
    }

    @GetMapping("/non/stream")
    public void stream(@RequestParam(name = "numberOfJobs")int numberOfJobs) {
        nonMdcService.stream(numberOfJobs);
    }


}
