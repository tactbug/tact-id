package com.tactbug.id.server.client;

import java.util.concurrent.ConcurrentHashMap;

public class IdFactory {

    private final ConcurrentHashMap<String, IdGenerator> ID_GENERATOR_MAP = new ConcurrentHashMap<>();

    public IdGenerator getOrBuildGenerator(
            String host,
            Integer port,
            String service,
            String domain,
            Integer quantity
    ){
        String key = service + ":" + domain;
        if (ID_GENERATOR_MAP.containsKey(key)){
            return ID_GENERATOR_MAP.get(key);
        }
        IdGenerator idGenerator = IdGenerator.build(host, port, service, domain, quantity);
        idGenerator.supplement();
        ID_GENERATOR_MAP.put(key, idGenerator);
        return idGenerator;
    }

}
