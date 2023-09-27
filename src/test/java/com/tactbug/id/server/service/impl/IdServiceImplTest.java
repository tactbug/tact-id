package com.tactbug.id.server.service.impl;

import com.tactbug.id.server.service.IdService;
import org.junit.jupiter.api.Test;

import java.util.Queue;
import java.util.concurrent.*;


public class IdServiceImplTest {

    private final IdService idService = new IdServiceImpl();

    private static final ConcurrentHashMap<Integer, Queue<Long>> TEST_RESULT = new ConcurrentHashMap<>();
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(4, 8, 2, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

    @Test
    public void test() throws InterruptedException { //并发获取ID测试, 并发数注意不能小于2
        String applicationName = "tact-recycle";
        String domainName = "recycleGoods";
        Integer quantity = 15000;
        int concurrent = 3;
        for (int i = 0; i < concurrent; i++) {
            int finalIndex = i;
            THREAD_POOL.execute(() -> {
                Queue<Long> snowflakeId = idService.getSnowflakeId(applicationName, domainName, quantity);
                TEST_RESULT.putIfAbsent(finalIndex, snowflakeId);
            });
        }
        Thread.sleep(5000);
        for (int i = 0; i < concurrent - 1; i++) {
            for (int j = i + 1; j < concurrent; j++) {
                int finalJ = j;
                assert TEST_RESULT.get(i).stream()
                        .noneMatch(i1 -> TEST_RESULT.get(finalJ).contains(i1));
            }
        }
    }
}