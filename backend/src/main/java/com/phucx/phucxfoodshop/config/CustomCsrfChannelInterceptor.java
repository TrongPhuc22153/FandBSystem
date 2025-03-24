package com.phucx.phucxfoodshop.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

// @Configuration
// @Order(Ordered.HIGHEST_PRECEDENCE+99)
public class CustomCsrfChannelInterceptor implements ChannelInterceptor{
    // private MessageMatcher<Object> matcher;
    // private Logger logger = LoggerFactory.getLogger(CustomCsrfChannelInterceptor.class);
    
    // public CustomCsrfChannelInterceptor() {
    //     this.matcher = new SimpMessageTypeMatcher(SimpMessageType.CONNECT);
    // }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // if(!matcher.matches(message)){
        //     return message;
        // }else{
            // LinkedMultiValueMap<String, String> nativeHeaders = 
            //     (LinkedMultiValueMap<String, String>) message.getHeaders().get("nativeHeaders");
            // logger.info(message.getHeaders().toString());
            // logger.info(nativeHeaders.getFirst("CsrfToken"));

            // Map<String, Object> sessionAttributes = SimpMessageHeaderAccessor.getSessionAttributes(message.getHeaders());
            // CsrfToken expectedToken = sessionAttributes != null ? (CsrfToken)sessionAttributes.get(CsrfToken.class.getName()) : null;
            // SimpMessageHeaderAccessor.wrap(message).getFirstNativeHeader(null);
            // logger.info(expectedToken.getHeaderName());

            // Map<String, Object> sessionAttributes = SimpMessageHeaderAccessor.getSessionAttributes(message.getHeaders());
            // CsrfToken expectedToken = sessionAttributes != null ? (CsrfToken)sessionAttributes.get(CsrfToken.class.getName()) : null;
            // if (expectedToken == null) {
            //    throw new MissingCsrfTokenException((String)null);
            // } else {
            //    String actualTokenValue = SimpMessageHeaderAccessor.wrap(message).getFirstNativeHeader(expectedToken.getHeaderName());
            //    boolean csrfCheckPassed = expectedToken.getToken().equals(actualTokenValue);
            //    if (!csrfCheckPassed) {
            //       throw new InvalidCsrfTokenException(expectedToken, actualTokenValue);
            //    } else {
            //       return message;
            //    }
            // }
        //     return message;

        // }
        return message;
    }
    
}
