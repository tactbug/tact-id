package com.tactbug.id.server.assist.util;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SnowflakeUtil {

    private final static long START_STMP = 1288834974657L;
    private final static long SEQUENCE_BIT = 12;  // 序列号占用的位数
    private final static long WORK_BIT = 10;    // 机器标识占用的位数
    private final static long MAX_WORK_NUM = ~(-1L << WORK_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private final static long WORK_LEFT = SEQUENCE_BIT;
    private final static long TIMESTMP_LEFT = WORK_LEFT + WORK_BIT;

    private final long workId;
    private long sequence = 0L;  //序列号
    private long lastStmp = -1L; //上一次时间戳

    public SnowflakeUtil(long workId) {
        if (workId > MAX_WORK_NUM || workId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_WORK_NUM or less than 0");
        }
        this.workId = workId;
    }

    /**
     * 产生下一个ID
     */
    public synchronized long nextId(long currentTime) {
        if (currentTime < lastStmp) {
            throw new UnsupportedOperationException("发生时钟回拨~");
        }

        if (currentTime == lastStmp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currentTime = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为 basicSequence
            sequence = 0;
        }

        lastStmp = currentTime;

        return (currentTime - START_STMP) << TIMESTMP_LEFT  // 时间戳部分
                | workId << WORK_LEFT                    // 节点部分
                | sequence;                              // 序列号部分
    }

    /**
     * 指定时间戳产生下一群ID
     */
    private Queue<Long> nextIdsInTime(long currentTime, Integer times) {
        Queue<Long> result  = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < times; i++) {
            long currentTimestamp = currentTime + i;
            for (int j = 0; j < 4000; j++) {
                Long id =  (currentTimestamp - START_STMP) << TIMESTMP_LEFT
                        | workId << WORK_LEFT
                        | j;
                result.add(id);
            }
        }
        return result;
    }

    /**
     * 指定数量产生下一群ID
     */
    public synchronized Queue<Long> nextIdsInQuantity(long currentTime, Integer quantity) {
        int times = quantity / 4000;
        int remainder = quantity % 4000;
        ConcurrentLinkedQueue<Long> result = new ConcurrentLinkedQueue<>(nextIdsInTime(currentTime, times));
        for (int i = 0; i < remainder; i++) {
            result.add(nextId(currentTime + 1000L));
        }
        return result;
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

}
