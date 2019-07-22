package com.yxy.webrtc.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class GetHttpSessionConfigurator extends Configurator{
    @Override  
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {  
        HttpSession httpSession = (HttpSession) request.getHttpSession();  
        if(httpSession != null){  
            config.getUserProperties().put(HttpSession.class.getName(), httpSession);  
        }  
    }  
}
