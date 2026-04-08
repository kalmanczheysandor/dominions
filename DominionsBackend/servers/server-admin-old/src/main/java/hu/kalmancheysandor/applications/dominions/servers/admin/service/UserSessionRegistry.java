package hu.kalmancheysandor.applications.dominions.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSessionRegistry {
    private final ConcurrentHashMap<String, String> userSessions = new ConcurrentHashMap<>();

    public void registerUser(String username, String sessionId) {
        userSessions.put(username, sessionId);
    }

    public String getSessionIdForUser(String username) {
        return userSessions.get(username);
    }

    public void removeUser(String username) {
        userSessions.remove(username);
    }
}
