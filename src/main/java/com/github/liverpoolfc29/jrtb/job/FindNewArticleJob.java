package com.github.liverpoolfc29.jrtb.job;


import com.github.liverpoolfc29.jrtb.service.FindNewArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Класс для EnableScheduling. В нем нужно создать в нем метод, который будет запускаться периодически. (добавить в ApplicationContext)
 * Job for finding new articles.
 */

@Component
@Slf4j
public class FindNewArticleJob {

    private final FindNewArticleService findNewArticleService;


    @Autowired
    public FindNewArticleJob(FindNewArticleService findNewArticleService) {
        this.findNewArticleService = findNewArticleService;
    }

    @Scheduled(fixedRateString = "${bot.recountNewArticleFixedRate}")
    public void findNewArticles() {

        LocalDateTime start = LocalDateTime.now();

        log.info("Find new article job started.");

        findNewArticleService.findNewArticles();

        LocalDateTime end = LocalDateTime.now();

        log.info("Find new articles job finished. Took seconds: {}",
                end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC));
    }

}
