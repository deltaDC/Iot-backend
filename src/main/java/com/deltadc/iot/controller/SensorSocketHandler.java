package com.deltadc.iot.controller;

import com.deltadc.iot.model.entities.Sensor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SensorSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);  // Track the connected session
        System.out.println("WebSocket connection established");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        sessions.remove(session);  // Remove the session on disconnect
        System.out.println("WebSocket connection closed");
    }

    public void broadcastSensorData(Sensor sensorData) throws IOException {
        for (WebSocketSession session : sessions) {
            sendSensorDataToSession(session, sensorData);
        }
    }

    private void sendSensorDataToSession(WebSocketSession session, Sensor sensorData) throws IOException {
        if (session.isOpen()) {
            String sensorDataJson = objectMapper.writeValueAsString(sensorData);
            session.sendMessage(new TextMessage(sensorDataJson));
        }
    }
}
