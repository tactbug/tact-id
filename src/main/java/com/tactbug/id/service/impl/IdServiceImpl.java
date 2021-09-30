package com.tactbug.id.service.impl;

import com.tactbug.id.assist.util.SnowflakeUtil;
import com.tactbug.id.domain.DomainSequence;
import com.tactbug.id.service.IdService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service
public class IdServiceImpl implements IdService {

    @Override
    public Queue<Long> getSnowflakeId(String applicationName, String aggregateName, Integer quantity) {
        if (quantity > 300000 || quantity < 10000){
            throw new IllegalArgumentException("每个主体每次请求ID数不能大于300,000个或是小于10,000个");
        }
        DomainSequence domainSequence = new DomainSequence(applicationName, aggregateName);
        String mapKey = applicationName + ":" + aggregateName;
        DOMAIN_SEQUENCE_MAP.putIfAbsent(mapKey, domainSequence);
        DomainSequence workMachine = DOMAIN_SEQUENCE_MAP.get(mapKey);
        Integer currentSequence = workMachine.getCurrentSequence();
        SnowflakeUtil snowflakeUtil = new SnowflakeUtil(currentSequence);
        long currentTimeMillis = System.currentTimeMillis();
        Queue<Long> ids = snowflakeUtil.nextIdsInQuantity(currentTimeMillis, quantity);
        SEQUENCE_SLEEP_TIME.put(domainSequence, currentTimeMillis);
        return ids;
    }

    @Scheduled(cron = "*/5 * * * * ?")
    public void initDomainSequence(){
        SEQUENCE_SLEEP_TIME.forEach((domainSequence, time) -> {
            if (System.currentTimeMillis() - time > 2 * 1000) {
                domainSequence.init();
            }
        });
    }

}
