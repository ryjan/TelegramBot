package org.ryjan.telegram.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ButtonCommandHandlerConfiguration {

    @Bean
    public ButtonCommandHandler buttonCommandHandler() {
        return new ButtonCommandHandler();
    }
}
