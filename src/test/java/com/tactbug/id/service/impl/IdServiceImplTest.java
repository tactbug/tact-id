package com.tactbug.id.service.impl;

import com.tactbug.id.service.IdService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.Resource;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class IdServiceImplTest {

    private final IdService idService = new IdServiceImpl();

    private static final ConcurrentHashMap<Integer, Queue<Long>> TEST_RESULT = new ConcurrentHashMap<>();
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(4, 8, 2, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

    @Test
    public void test() throws InterruptedException {
        String applicationName = "tact-recycle";
        String domainName = "recycleGoods";
        Integer quantity = 20000;
        for (int i = 0; i < 10; i++) {
            int finalIndex = i;
            THREAD_POOL.execute(() -> {
                Queue<Long> snowflakeId = idService.getSnowflakeId(applicationName, domainName, quantity);
                TEST_RESULT.putIfAbsent(finalIndex, snowflakeId);
            });
        }
        Thread.sleep(5000);
        for (int i = 0; i < 9; i++) {
            for (int j = i + 1; j < 10; j++) {
                int finalJ = j;
                assert TEST_RESULT.get(i).stream()
                        .noneMatch(i1 -> TEST_RESULT.get(finalJ).contains(i1));
            }
        }
    }
}