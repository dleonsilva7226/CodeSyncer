package com.codesyncer.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SimpleWebSocketHandler extends TextWebSocketHandler {

    private final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();;

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String email = getEmailFromSession(session);
        if (email != null) {
            sessionMap.put(email, session);
            log.info("Websocket connected for id: " + email);
        }
    }

    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String email = getEmailFromSession(session);
        String receivedMessage = (String) message.getPayload();
        if (email != null) {
            log.info("Received message from " + email + ": " + receivedMessage);
            // Echo the message back to the client
            session.send(new TextMessage("Echo: " + receivedMessage));
        }
    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String email = getEmailFromSession(session);
        if (email != null) {
            sessionMap.remove(email);
            log.info("Websocket disconnected for id: " + email);
        }
    }

    private String getEmailFromSession(WebSocketSession session) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(session.getUri());
        Map<String,String> queryParams = builder.build().getQueryParams().toSingleValueMap();
        return queryParams.get("email");
    }
}

