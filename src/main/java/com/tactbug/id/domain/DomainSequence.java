package com.tactbug.id.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(exclude = {"sequenceMap", "createTime", "updateTime"})
public class DomainSequence {

    private String applicationName;
    private String domainName;
    private volatile ConcurrentHashMap<Integer, Integer> sequenceMap = new ConcurrentHashMap<>(MAX_SEQUENCE);

    private ZonedDateTime createTime;
    private volatile ZonedDateTime updateTime;

    private static final Integer MAX_SEQUENCE = 1023;
    private static final Integer IN_USE = 1;
    private static final Integer IDLE = 2;
    private static final Integer FAIL_TAG = -1;

    public DomainSequence(String applicationName, String domainName){
        this.applicationName = applicationName;
        this.domainName = domainName;

        createTime = ZonedDateTime.now();
        updateTime = ZonedDateTime.now();
    }

    public synchronized void init(){
        if (!sequenceMap.isEmpty()){
            sequenceMap.clear();
            updateTime = ZonedDateTime.now();
        }
    }

    public Integer getCurrentSequence(){
        Integer sequence = generate();
        while (sequence.equals(FAIL_TAG)){
            sequence = generate();
        }
        return sequence;
    }

    private synchronized Integer generate(){
        if (sequenceMap.isEmpty()){
            sequenceMap.put(1, IN_USE);
            return 1;
        }
        int nextSequence = 1;
        List<Integer> keyList = sequenceMap.keySet().stream().sorted().collect(Collectors.toList());
        for (int i = 1; i <= keyList.size(); i++) {
            if (sequenceMap.get(i).equals(IDLE)){
                sequenceMap.put(i, IN_USE);
                return i;
            }else {
                nextSequence += 1;
            }
        }
        if (nextSequence <= MAX_SEQUENCE){
            sequenceMap.put(nextSequence, IN_USE);
            return nextSequence;
        }
        return -1;
    }
}
