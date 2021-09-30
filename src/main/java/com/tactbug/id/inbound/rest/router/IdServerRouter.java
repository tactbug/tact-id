package com.tactbug.id.inbound.rest.router;

import com.tactbug.id.inbound.rest.handler.IdServerHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * @author ：tactbug@gmail.com
 * @date ：Created in 2021/9/29 15:08
 */
@Configuration
public class IdServerRouter {

    @Value("${server.servlet.context-path}")
    private String prePath;

    @Bean
    public RouterFunction<ServerResponse> route(IdServerHandler idServerHandler) {
        return RouterFunctions.route()
                .GET(
                    prePath + "/snowflake/batch/{applicationName}/{aggregateName}/{quantity}",
                    accept(MediaType.APPLICATION_JSON),
                    idServerHandler::getBatchIds
                )
                .build();
    }
}
