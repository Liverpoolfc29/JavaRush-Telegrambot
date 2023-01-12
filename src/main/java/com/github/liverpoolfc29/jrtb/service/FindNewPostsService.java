package com.github.liverpoolfc29.jrtb.service;

/**
 * Service for finding new Posts.
 */

public interface FindNewPostsService {

    /**
     * Find new Posts and notify subscribers about it.
     */
    void findNewPosts();
}
