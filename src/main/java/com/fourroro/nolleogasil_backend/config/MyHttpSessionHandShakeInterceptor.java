package com.fourroro.nolleogasil_backend.config;

import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class MyHttpSessionHandShakeInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);

            if (session != null) {
                // Assume 'users' is a session attribute that holds a UsersDto object
                UsersDto users = (UsersDto) session.getAttribute("users");
                if (users != null) {
                    // You can decide to store the entire UsersDto object or just specific attributes
                    attributes.put("users", users);
                    // Alternatively, just store the user ID if that's all you need
                    attributes.put("userId", users.getUsersId());
                }
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
