package com.seven.userse.config;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    // Spring lifecycle method to initialize Twilio after properties are set
    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }
}
