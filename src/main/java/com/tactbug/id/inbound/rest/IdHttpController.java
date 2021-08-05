package com.tactbug.id.inbound.rest;

import com.tactbug.id.inbound.rest.vo.Result;
import com.tactbug.id.service.IdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Queue;

@RestController
@RequestMapping("/snowflake")
public class IdHttpController {

    @Autowired
    private IdService idService;

    @GetMapping("/batch/{application}/{domain}/{quantity}")
    public Queue<Long> getBatchIds(
            @PathVariable String application,
            @PathVariable String domain,
            @PathVariable String quantity
    ){
        int i = Integer.parseInt(quantity);
        return idService.getSnowflakeId(application, domain, i);
    }
}
