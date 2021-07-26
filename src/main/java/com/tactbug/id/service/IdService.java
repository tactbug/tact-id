package com.tactbug.id.service;

import java.util.List;

public interface IdService {
    List<Long> getSnowflakeId(String applicationName, String domainName, Integer quantity);
}
