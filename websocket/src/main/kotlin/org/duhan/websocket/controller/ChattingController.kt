package org.duhan.websocket.controller

import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.util.HtmlUtils

@Controller
class ChattingController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @MessageMapping("/chatting-message")
    @SendTo("/topic/chatting")
    fun chatting(chattingMessage: ChattingMessage): ChattingResponse {
        logger.info(">>> Received Message: ${chattingMessage.message}")
        return ChattingResponse(HtmlUtils.htmlEscape(chattingMessage.message))
    }
}