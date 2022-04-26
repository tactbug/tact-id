package com.tactbug.id.server.inbound.rest;

import com.tactbug.id.server.service.IdService;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Queue;

/**
 * @Author tactbug
 * @Email tactbug@Gmail.com
 * @Time 2021/10/3 23:38
 */
@RestController
public class IdServerController {

    @Resource
    private IdService idService;

    @GetMapping("/batch/{application}/{aggregate}/{quantity}")
    public Mono<Queue<Long>> snowflakeBatch(
            @PathVariable("application") String application,
            @PathVariable("aggregate") String aggregate,
            @PathVariable("quantity") @DefaultValue("10000") Integer quantity
    ){
        return Mono.just(idService.getSnowflakeId(application, aggregate, quantity));
    }

    @GetMapping("/hello")
    public Mono<String> hello(){
        return Mono.just("hello");
    }

}
