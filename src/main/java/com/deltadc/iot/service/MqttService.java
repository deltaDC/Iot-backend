package com.deltadc.iot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MqttService {

    private final MqttClient mqttClient;

    /**
     * Publish a message to a topic
     *
     * @param topic   String
     * @param message String
     */
    public void publish(String topic, String message) {
        try {
            mqttClient.publish(topic, message.getBytes(), 0, false);
        } catch (Exception e) {
            log.error("Failed to publish to topic", e);
        }
    }


    /**
     * Subscribe to a topic and wait for a message response
     *
     * @param topic String
     * @return The message received
     */
    public String subscribeAndWaitForMessage(String topic) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = new CompletableFuture<>();
        try {
            mqttClient.subscribe(topic, (t, msg) -> {
                String message = new String(msg.getPayload());
                future.complete(message);
            });
        } catch (MqttException e) {
            log.error("Failed to subscribe to topic", e);
            future.completeExceptionally(e);
        }
        return future.get();
    }
}
