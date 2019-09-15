package ro.apisec.ms.demo.config.throttling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final LoginAttemptWatcher loginAttemptWatcher;

    @Autowired
    public AuthSuccessEventListener(final LoginAttemptWatcher loginAttemptWatcher) {
        this.loginAttemptWatcher = Objects.requireNonNull(loginAttemptWatcher, "loginAttemptService must not be null");
    }

    public void onApplicationEvent(final AuthenticationSuccessEvent event) {
        loginAttemptWatcher.loginSucceeded(String.valueOf(event.getAuthentication().getPrincipal()));
    }
}