package org.duhan.websocket.config

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent

@Component
class WebSocketEventHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun handleWebSocketSessionConnectEventListener(event: SessionConnectEvent) {
        logger.info(">>> SessionConnect")
    }

    @EventListener
    fun handleWebSocketSessionSubscribeEventListener(event: SessionSubscribeEvent) {
        logger.info(">>> SessionSubscribe")
    }

    @EventListener
    fun handleWebSocketSessionUnsubscribeEventListener(event: SessionUnsubscribeEvent) {
        logger.info(">>> SessionUnsubscribe")
    }

    @EventListener
    fun handleWebSocketSessionDisconnectEventListener(event: SessionDisconnectEvent) {
        logger.info(">>> SessionDisconnect")
    }
}