package in.docq.health.facility.service;

import com.google.gson.Gson;
import in.docq.health.facility.model.HealthProfessional;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.*;

@Component
@Slf4j
public class WsConnectionHandler extends TextWebSocketHandler {
    private final Gson gson = new Gson();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, Long> lastPongTimestamps = new ConcurrentHashMap<>();
    private final ExecutorService messageSenderExecutor = Executors.newFixedThreadPool(20);
    private final ScheduledExecutorService deadSessionCleaner = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService pingExecutorService = Executors.newSingleThreadScheduledExecutor();

    public WsConnectionHandler() {
        pingExecutorService.scheduleAtFixedRate(this::pingAllClients, 5, 30, TimeUnit.SECONDS);
        deadSessionCleaner.scheduleAtFixedRate(this::cleanDeadSessions, 1, 1, TimeUnit.MINUTES);
    }

    private void cleanDeadSessions() {
        long now = System.currentTimeMillis();
        for (Map.Entry<String, Long> entry : lastPongTimestamps.entrySet()) {
            if (now - entry.getValue() > 2 * 60 * 1000) { // 2 minutes
                String userId = entry.getKey();
                WebSocketSession session = sessions.get(userId);
                if (session != null) {
                    log.info("Closing dead session for user {}", userId);
                    closeSessionQuietly(session);
                }
                sessions.remove(userId);
                lastPongTimestamps.remove(userId);
            }
        }
    }

    private void pingAllClients() {
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                sendMessage(session, new PingMessage(), 5000);
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        sessions.put(userId, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = (String) session.getAttributes().get("userId");
        sessions.remove(userId);
    }

    @Override
    public void handlePongMessage(WebSocketSession session, PongMessage message) {
        lastPongTimestamps.put((String) session.getAttributes().get("userId"), System.currentTimeMillis());
    }

    private void sendMessage(WebSocketSession session, WebSocketMessage<?> message, int timeoutMillis) {
        if (session == null || !session.isOpen()) {
            return;
        }

        Future<?> future = messageSenderExecutor.submit(() -> {
            try {
                session.sendMessage(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            future.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            log.warn("Timed out sending message to session {}", session.getId());
            future.cancel(true);
            closeSessionQuietly(session);
        } catch (Exception e) {
            log.warn("Failed to send message to session {}: {}", session.getId(), e.getMessage());
            closeSessionQuietly(session);
        }
    }

    private void sendMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (session == null || !session.isOpen()) {
            return;
        }

        messageSenderExecutor.submit(() -> {
            try {
                session.sendMessage(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void closeSessionQuietly(WebSocketSession session) {
        try {
            session.close();
        } catch (Exception ignored) {}
    }

    public void sendMessage(HealthProfessional healthProfessional, StateChangeMessage message) {
        WebSocketSession session = sessions.get(healthProfessional.getKeyCloakUserName());
        if (session != null && session.isOpen()) {
            String json = gson.toJson(message);
            sendMessage(session, new TextMessage(json));
        }
    }

    public Long getLastPongTimestamp(HealthProfessional healthProfessional) {
        return lastPongTimestamps.get(healthProfessional.getKeyCloakUserName());
    }

    @Builder
    public static class StateChangeMessage {
        private String objectId;
        private String objectType;
        private String fromState;
        private String toState;
    }
}

