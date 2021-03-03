package com.example.salle.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketBrokerConfig.class);

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		//for subscribe prefix
		registry.enableSimpleBroker("/user");
		//for publish prefix
		registry.setApplicationDestinationPrefixes("/app");
		//user destination provides ability to have unique user queue
		//registry.setUserDestinationPrefix("/user");
		
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		
		registry.addEndpoint("/broadcast")
			.setAllowedOrigins("http://ec2....amazonaws.com")	
			.withSockJS()
			.setHeartbeatTime(60_000);
	}

}
