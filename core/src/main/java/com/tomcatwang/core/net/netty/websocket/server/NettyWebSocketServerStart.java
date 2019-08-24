package com.tomcatwang.core.net.netty.websocket.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class NettyWebSocketServerStart {

    @Value("${netty.websocket.server.port}")
    private String port;


    @PostConstruct
    public void serverStart() throws Exception {
        NettyWebSocketServer server = new NettyWebSocketServer();
        server.setPort("12345");
        server.start();
    }

}
