package com.nighthawk.spring_portfolio.mvc.websocket;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.List;
import java.util.ArrayList;



@Component
public class UserQueueManager {

    private final Queue<SocketIOClient> userQueue = new LinkedList<>();
    private final ConcurrentHashMap<SocketIOClient, Integer> clientToQueueIdMap = new ConcurrentHashMap<>();
    private final AtomicInteger queueId = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, SocketIOClient> userIdToClientMap = new ConcurrentHashMap<>();

    

    // Add user to the queue and return their queue ID
    public int enqueueUser(SocketIOClient client, int userId) { // adds user to queue
        int id = queueId.incrementAndGet();
        userQueue.add(client);
        clientToQueueIdMap.put(client, id);
        userIdToClientMap.put(userId, client);
        return id;
    }

    // Remove the first user from the queue
    public SocketIOClient dequeueUser() {
        SocketIOClient client = userQueue.poll();
        if (client != null) {
            clientToQueueIdMap.remove(client);
            return client;
        }
        return null;
    }

    public void removeUser(SocketIOClient client, int userId) {
        userQueue.remove(client); // removes users from the queue
        clientToQueueIdMap.remove(client);
        userIdToClientMap.remove(userId);
    }

    // Check the queue ID of the next user
    public int peekQueueId() {
        SocketIOClient client = userQueue.peek();
        return client != null ? clientToQueueIdMap.getOrDefault(client, -1) : -1;
    }

    // Get all user IDs in the queue
    public Queue<Integer> getAllUserIds() {
        Queue<Integer> ids = new LinkedList<>();
        for (SocketIOClient client : userQueue) {
            Integer id = clientToQueueIdMap.get(client);
            if (id != null) { // if there is an id that exists, add it to queue and make sure that it is in the list of all ids when you get it
                ids.add(id);
            }
        }
        return ids;
    }

    public List<String> viewQueue() {
        List<String> sessionIds = new ArrayList<>();
        for (SocketIOClient client : userQueue) {
            sessionIds.add(client.getSessionId().toString());
        }
        return sessionIds;
    }

    public boolean isUserIdInQueue(int userId) {
        return userIdToClientMap.containsKey(userId);
    }
    
}
