package com.richard.service;

import com.richard.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ParallelRestService {

    private static final Logger logger = LoggerFactory.getLogger(ParallelRestService.class);

    private final RestTemplate restTemplate;

    public ParallelRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ObjectRequest> requestParallel(List<ObjectRequest> requests) {

        List<CompletableFuture<ObjectRequest>> futures = requests.stream()
                                                                 .map(request -> getAsync(request))
                                                                 .collect(Collectors.toList());
        List<ObjectRequest> result = futures.stream()
                                            .map(CompletableFuture::join)
                                            .collect(Collectors.toList());
        return result;
    }

    private CompletableFuture<ObjectRequest> getAsync(ObjectRequest objectRequest) {

        CompletableFuture<ObjectRequest> future = CompletableFuture.supplyAsync(new Supplier<ObjectRequest>() {
            @Override
            public ObjectRequest get() {
                objectRequest.get();
                return objectRequest;
            }
        });

        return future;
    }

    public List<User> requestParallelUser(List<String> requests) {

        List<CompletableFuture<User>> futures = requests.stream()
                                            .map(request -> getAsyncUser(request))
                                            .collect(Collectors.toList());
        List<User> result =
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());

        return result;
    }

    private CompletableFuture<User> getAsyncUser(String names) {

        CompletableFuture<User> future = CompletableFuture.supplyAsync(new Supplier<User>() {

            @Override
            public User get() {
                String url = String.format("https://api.github.com/users/%s", names);
                User response = restTemplate.getForObject(url, User.class);
                return response;
            }
        });

        return future;
    }


    //@Async
    public CompletableFuture<User> findUser(String user) throws InterruptedException {
        logger.info("Looking up " + user);
        String url = String.format("https://api.github.com/users/%s", user);
        User results = restTemplate.getForObject(url, User.class);
        //Thread.sleep(1000L);
        return CompletableFuture.completedFuture(results);
    }
}
