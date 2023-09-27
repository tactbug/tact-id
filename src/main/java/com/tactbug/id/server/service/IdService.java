package com.tactbug.id.server.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public interface IdService {
    ConcurrentHashMap<String, List<ZonedDateTime>> DOMAIN_MACHINE_ID_POOL = new ConcurrentHashMap<>();
    Queue<Long> getSnowflakeId(String applicationName, String aggregateName, Integer quantity);
}
