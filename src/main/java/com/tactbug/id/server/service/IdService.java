package com.tactbug.id.server.service;

import com.tactbug.id.server.domain.DomainSequence;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public interface IdService {
    ConcurrentHashMap<String, DomainSequence> DOMAIN_SEQUENCE_MAP = new ConcurrentHashMap<>();
    ConcurrentHashMap<DomainSequence, Long> SEQUENCE_SLEEP_TIME = new ConcurrentHashMap<>();

    Queue<Long> getSnowflakeId(String applicationName, String aggregateName, Integer quantity);
}
