package com.yapp.crew.config;

import com.yapp.crew.auth.Auth;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.websocket.CustomHandshakeInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//	private final Auth auth;
//
//	private final UserRepository userRepository;
//
//	@Autowired
//	public WebSocketConfig(Auth auth, UserRepository userRepository) {
//		this.auth = auth;
//		this.userRepository = userRepository;
//	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
				.addEndpoint("/ws/chat")
//				.addInterceptors(new CustomHandshakeInterceptor(auth, userRepository))
				.setAllowedOrigins("*")
				.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry
				.setApplicationDestinationPrefixes("/pub")
				.enableSimpleBroker("/sub");
	}
}
