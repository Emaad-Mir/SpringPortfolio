package com.nighthawk.spring_portfolio.mvc.websocket;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UserQueueManager {

    private Queue<SocketIOClient> userQueue = new LinkedList<>();
    private AtomicInteger queueId = new AtomicInteger(0);
    
    // Add user to the queue and return their queue ID
    public int enqueueUser(SocketIOClient client) {
        userQueue.add(client);
        return queueId.incrementAndGet();
    }

    // Remove the first user from the queue
    public SocketIOClient dequeueUser() {
        return userQueue.poll();
    }

    // Check the queue ID of the next user
    public int peekQueueId() {
        return queueId.get();
    }
}
