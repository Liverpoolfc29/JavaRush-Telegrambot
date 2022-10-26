package com.github.liverpoolfc29.jrtb.javarushclient.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * Request arguments for group requests.
 */

@Builder
@Getter
public class GroupRequestArgs {
    private final String query;
    private final GroupInfoType type;
    private final GroupFilter filter;
    private final Integer offset;       // specified where to start getting groups
    private final Integer limit;       // Limited number of groups.

    public Map<String, Object> populateQueries() {
        Map<String, Object> queries = new HashMap<>();
        if (nonNull(query)) {
            queries.put("query", query);
        }
        if (nonNull(type)) {
            queries.put("type", type);
        }
        if (nonNull(filter)) {
            queries.put("filter", filter);
        }
        if (nonNull(offset)) {
            queries.put("offset", offset);
        }
        if (nonNull(limit)) {
            queries.put("limit", limit);
        }
        return queries;
    }

}