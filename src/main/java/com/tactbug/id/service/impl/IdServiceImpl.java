package com.tactbug.id.service.impl;

import com.tactbug.id.assist.util.SnowflakeUtil;
import com.tactbug.id.domain.DomainSequence;
import com.tactbug.id.service.IdService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdServiceImpl implements IdService {

    public static final ConcurrentHashMap<String, DomainSequence> DOMAIN_SEQUENCE_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<DomainSequence, Long> SEQUENCE_SLEEP_TIME = new ConcurrentHashMap<>();

    @Override
    public List<Long> getSnowflakeId(String applicationName, String domainName, Integer quantity) {
        if (quantity > 300000 || quantity < 10000){
            throw new IllegalArgumentException("每个主体每次请求ID数不能大于300,000个或是小于10,000个");
        }
        DomainSequence domainSequence;
        String mapKey = applicationName + ":" + domainName;
        if (DOMAIN_SEQUENCE_MAP.containsKey(mapKey)){
            domainSequence = DOMAIN_SEQUENCE_MAP.get(mapKey);
        }else {
            domainSequence = new DomainSequence(applicationName, domainName);
            DOMAIN_SEQUENCE_MAP.put(mapKey, domainSequence);
        }
        SnowflakeUtil snowflakeUtil = new SnowflakeUtil(domainSequence.getCurrentSequence());
        long currentTimeMillis = System.currentTimeMillis();
        List<Long> ids = snowflakeUtil.nextIdsInQuantity(currentTimeMillis, quantity);
        SEQUENCE_SLEEP_TIME.put(domainSequence, currentTimeMillis);
        return ids;
    }

    @Scheduled(cron = "*/5 * * * * ?")
    public void initDomainSequence(){
        System.out.println("start init ~~~");
        SEQUENCE_SLEEP_TIME.forEach((domainSequence, time) -> {
            if (System.currentTimeMillis() - time > 2 * 1000) {
                domainSequence.init();
            }
        });
    }

}
