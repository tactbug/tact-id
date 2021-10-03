package com.tactbug.id.assist.demo;

import reactor.core.publisher.Flux;

import java.util.function.Consumer;

/**
 * @Author tactbug
 * @Email tactbug@Gmail.com
 * @Time 2021/10/3 23:03
 */
public class ReactorDemo {
    public static void main(String[] args) {
        Flux<String> flux = Flux.just("a", "b", "c", "d");
        flux.subscribe(s -> System.out.println("消费: " + s), throwable -> System.out.println("发生异常: " + throwable)
        );
    }
}
