package com.tactbug.id.server.service.impl;

import com.tactbug.id.server.assist.util.SnowflakeUtil;
import com.tactbug.id.server.service.IdService;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

@Service
public class IdServiceImpl implements IdService {

    @Override
    public Queue<Long> getSnowflakeId(String applicationName, String aggregateName, Integer quantity) {
        if (quantity > 300000 || quantity < 10000){
            throw new IllegalArgumentException("每个主体每次请求ID数不能大于300,000个或者小于10000个");
        }
        String key = applicationName + ":" + aggregateName;
        DOMAIN_MACHINE_ID_POOL.putIfAbsent(key, new ArrayList<>(Collections.nCopies(1023, null)));
        List<ZonedDateTime> machinePool = DOMAIN_MACHINE_ID_POOL.get(key);
        SnowflakeUtil snowflakeUtil = new SnowflakeUtil(getMachineId(machinePool));
        long currentTimeMillis = System.currentTimeMillis();
        return snowflakeUtil.nextIdsInQuantity(currentTimeMillis, quantity);
    }

    private synchronized Integer getMachineId(List<ZonedDateTime> list){
        while (true){
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i).plusSeconds(2L).isBefore(ZonedDateTime.now())){
                    list.set(i, ZonedDateTime.now());
                    return i;
                }
            }
        }
    }

}
