package org.ryjan.telegram.kafka;

import org.ryjan.telegram.services.ServiceBuilder;
import org.springframework.stereotype.Service;

@Service
public class BlacklistProducer extends ServiceBuilder {
    public static String FIND_BLACKLIST_TOPIC = "find-blacklist-topic";
}
