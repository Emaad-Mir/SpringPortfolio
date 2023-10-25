package com.nighthawk.spring_portfolio.mvc.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.corundumstudio.socketio.SocketIOClient;

@RestController
@RequestMapping("/queue")
public class QueueController {
    
    @Autowired
    private UserQueueManager userQueueManager;

    @GetMapping("/dequeue")
    public ResponseEntity<String> dequeueUser() {
        SocketIOClient client = userQueueManager.dequeueUser();
        if (client != null) {
            return ResponseEntity.ok("Dequeued user with session ID: " + client.getSessionId());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No users in queue");
    }

    @GetMapping("/peek")
    public ResponseEntity<String> peekQueueId() {
        int id = userQueueManager.peekQueueId();
        return ResponseEntity.ok("Next user in queue has ID: " + id);
    }
}