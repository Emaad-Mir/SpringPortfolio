package com.nighthawk.spring_portfolio.mvc.websocket;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


import com.corundumstudio.socketio.SocketIOClient;

@RestController
@RequestMapping("/queue")
public class QueueController {

    @Autowired
    private UserQueueManager userQueueManager;

    @PostMapping("/dequeue")
    public ResponseEntity<String> dequeueUser() {
        try {
            SocketIOClient client = userQueueManager.dequeueUser();
            if (client != null) {
                return ResponseEntity.ok("Dequeued user with session ID: " + client.getSessionId());
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No users in queue");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in dequeuing user");
        }
    }


    @GetMapping("/peek")
    public ResponseEntity<String> peekQueueId() {
        int id = userQueueManager.peekQueueId();
        return ResponseEntity.ok("Next user in queue has ID: " + id);
    }

    @GetMapping("/view")
    public ResponseEntity<List<String>> viewQueue() {
        List<String> users = userQueueManager.viewQueue();
        System.out.println("View Queue: " + users); // Debug log
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(users);
        }
    return ResponseEntity.ok(users);
}
    @GetMapping("/checkUser") // checks if a user (specifically id) exists using the endpoint
    public ResponseEntity<String> checkIfUserExists(@RequestParam int userId) {
        try {
            boolean exists = userQueueManager.isUserIdInQueue(userId);
            if (exists) {
                return ResponseEntity.ok("User with ID: " + userId + " exists in the queue.");
            } else {
                return ResponseEntity.ok("User with ID: " + userId + " does not exist in the queue.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking user: " + e.getMessage());
        }
    }


}