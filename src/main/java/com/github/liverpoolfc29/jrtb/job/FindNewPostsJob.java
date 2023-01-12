package com.github.liverpoolfc29.jrtb.job;


import com.github.liverpoolfc29.jrtb.service.FindNewPostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Класс для EnableScheduling. В нем нужно создать в нем метод, который будет запускаться периодически. (добавить в ApplicationContext)
 * Job for finding new posts.
 */

@Component
@Slf4j
public class FindNewPostsJob {

    private final FindNewPostsService findNewPostsService;


    @Autowired
    public FindNewPostsJob(FindNewPostsService findNewPostsService) {
        this.findNewPostsService = findNewPostsService;
    }

    @Scheduled(fixedRateString = "${bot.recountNewPostFixedRate}")
    public void findNewPosts() {

        LocalDateTime start = LocalDateTime.now();

        log.info("Find new Posts job started.");

        findNewPostsService.findNewPosts();

        LocalDateTime end = LocalDateTime.now();

        log.info("Find new Posts job finished. Took seconds: {}",
                end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC));
    }

}
