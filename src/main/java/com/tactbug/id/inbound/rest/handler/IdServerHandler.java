package com.tactbug.id.inbound.rest.handler;

import com.tactbug.id.service.IdService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author ：tactbug@gmail.com
 * @date ：Created in 2021/9/29 15:07
 */
@Component
public class IdServerHandler {

    @Resource
    private IdService idService;

    public Mono<ServerResponse> getBatchIds(ServerRequest request) {
        checkRequest(request);
        String applicationName = request.pathVariable("applicationName");
        String aggregateName = request.pathVariable("aggregateName");
        int quantity;
        try {
            quantity = Integer.parseInt(request.pathVariable("quantity"));
        }catch (Exception e){
            quantity = 10000;
        }
        Mono<Queue<Long>> mono = Mono.just(idService.getSnowflakeId(applicationName, aggregateName, quantity));
        return mono
                .flatMap(q -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(q))
                .switchIfEmpty(ServerResponse.noContent().build());
    }

    private void checkRequest(ServerRequest request){
        Map<String, String> attributes = request.pathVariables();
        if (!attributes.containsKey("applicationName")){
            throw new IllegalArgumentException("请求缺少服务名");
        }
        if (!attributes.containsKey("aggregateName")){
            throw new IllegalArgumentException("请求缺少对象名");
        }
        String applicationName = attributes.get("applicationName");
        String aggregateName = attributes.get("aggregateName");
        if (Objects.isNull(applicationName) || applicationName.isBlank()){
            throw new IllegalArgumentException("服务名不能为空");
        }
        if (Objects.isNull(aggregateName) || aggregateName.isBlank()){
            throw new IllegalArgumentException("对象名不能为空");
        }
    }
}
