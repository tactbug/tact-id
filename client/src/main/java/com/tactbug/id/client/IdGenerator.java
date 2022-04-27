package com.tactbug.id.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.PriorityBlockingQueue;

public class IdGenerator {
    private String baseUrl;
    private Integer quantity;
    private volatile PriorityBlockingQueue<Long> idQueue;

    protected IdGenerator(){}

    public static IdGenerator build(String host, Integer port, String service, String domain, Integer quantity){
        IdGenerator idGenerator = new IdGenerator();
        if (Objects.isNull(quantity) || quantity.compareTo(1) < 0){
            quantity = 100;
        }
        String url = "http://" + host + ":" + port + "/id/batch/" + service + "/" + domain;
        idGenerator.setBaseUrl(url);
        idGenerator.setQuantity(quantity);
        idGenerator.setIdQueue(new PriorityBlockingQueue<>(quantity));
        return idGenerator;
    }

    public Long nextId(){
        Long id;
        if (!idQueue.isEmpty()){
            id = idQueue.poll();
            if (idQueue.size() < quantity / 2){
                supplement();
            }
        }else {
            supplement();
            id = idQueue.poll();
        }
        return id;
    }

    protected synchronized void supplement(){
        HttpClient httpClient = HttpClient.newHttpClient();
        int supplement = quantity - idQueue.size();
        String url = baseUrl + "/" + supplement;
        try {
            HttpResponse<String> response = httpClient.send(request(url), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            String body = response.body();
            String substring = body.substring(1, body.length() - 1);
            Arrays.stream(substring.split(","))
                            .forEach(id -> idQueue.add(Long.valueOf(id)));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private HttpRequest request(String url){
        return HttpRequest.newBuilder()
                .GET()
                .timeout(Duration.of(3L, ChronoUnit.SECONDS))
                .uri(URI.create(url))
                .build();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public PriorityBlockingQueue<Long> getIdQueue() {
        return idQueue;
    }

    public void setIdQueue(PriorityBlockingQueue<Long> idQueue) {
        this.idQueue = idQueue;
    }
}
