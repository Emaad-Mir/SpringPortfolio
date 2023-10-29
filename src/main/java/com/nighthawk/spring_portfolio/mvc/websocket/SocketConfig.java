package com.nighthawk.spring_portfolio.mvc.websocket;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Log4j2
@Component
public class SocketConfig {

    @Value("${socket.port}")
    private int SOCKETPORT;

    @Value("${socket.host}")
    private String SOCKETHOST;

    private SocketIOServer server;

    @Autowired
    private final UserQueueManager userQueueManager;

    @Autowired
    public SocketConfig(UserQueueManager userQueueManager) {
        this.userQueueManager = userQueueManager;
    }

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(SOCKETHOST);
        config.setPort(SOCKETPORT);
        server = new SocketIOServer(config);
        server.start();

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                int userId = client.getSessionId().toString().hashCode(); // Generating a user ID
                int queueId = userQueueManager.enqueueUser(client, userId);
                log.info("User with socket " + client.getSessionId() + " assigned queue ID: " + queueId);
            } 
        });

        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                int userId = client.getSessionId().toString().hashCode(); // Generating the same user ID
                userQueueManager.removeUser(client, userId); // Update to match method signature
                log.info("User disconnected " + client.getSessionId().toString());
            }
        });
        return server;
    }

    @PreDestroy
    public void stopSocketIOServer() {
        this.server.stop();
    }
}
