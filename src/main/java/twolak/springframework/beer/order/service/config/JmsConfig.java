package twolak.springframework.beer.order.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

/**
 *
 * @author twolak
 */
@Configuration
public class JmsConfig {

    public static final String VALIDATE_ORDER_QUEUE = "validate-order";
    
    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter jackson2MessageConverter = new MappingJackson2MessageConverter();
        jackson2MessageConverter.setTargetType(MessageType.TEXT);
        jackson2MessageConverter.setTypeIdPropertyName("_type");
        jackson2MessageConverter.setObjectMapper(objectMapper);
        return jackson2MessageConverter;
    }
}
