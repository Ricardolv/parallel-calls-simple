package com.richard;

import com.richard.service.ObjectRequest;
import com.richard.service.ParallelRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final ParallelRestService parallelRestService;
    private final RestTemplate restTemplate;

    public AppRunner(ParallelRestService parallelRestService, RestTemplate restTemplate) {
        this.parallelRestService = parallelRestService;
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        long start = System.currentTimeMillis();

        String urlPivotalSoftware = "https://api.github.com/users/PivotalSoftware";
        String urlCloudFoundry = "https://api.github.com/users/CloudFoundry";
        String urlSpringProjects = "https://api.github.com/users/Spring-Projects";

        ObjectRequest objectRequest1 = new ObjectRequest(urlPivotalSoftware, restTemplate);
        ObjectRequest objectRequest2 = new ObjectRequest(urlCloudFoundry, restTemplate);
        ObjectRequest objectRequest3 = new ObjectRequest(urlSpringProjects, restTemplate);

        List<ObjectRequest> objectRequests = parallelRestService.requestParallel(Arrays.asList(objectRequest1, objectRequest2, objectRequest3));

        objectRequests.stream().forEach(objectRequest -> {
            System.out.println(objectRequest.getResponseEntity().getBody());
        });


//        List<User> users = parallelRestService.requestParallelUser(Arrays.asList("PivotalSoftware", "CloudFoundry", "Spring-Projects"));
//
//        users.stream().forEach(user -> System.out.println(user.toString()));

//        CompletableFuture<User> page1 = parallelRestService.findUser("PivotalSoftware");
//        CompletableFuture<User> page2 = parallelRestService.findUser("CloudFoundry");
//        CompletableFuture<User> page3 = parallelRestService.findUser("Spring-Projects");
//
//        CompletableFuture.allOf(page1,page2,page3).join();
//
//        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
//        logger.info("--> " + page1.get());
//        logger.info("--> " + page2.get());
//        logger.info("--> " + page3.get());

    }
}
