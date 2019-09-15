package ro.apisec.ms.demo.config.throttling;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptWatcher loginAttemptWatcher;
    private final BackendThrottler backendThrottler;

    @Autowired
    public AuthFailureEventListener(final LoginAttemptWatcher loginAttemptWatcher,
                                    final BackendThrottler backendThrottler) {
        this.loginAttemptWatcher = Objects.requireNonNull(loginAttemptWatcher, "loginAttemptService must not be null");
        this.backendThrottler = Objects.requireNonNull(backendThrottler, "backendThrottler must not be null");
    }

    public void onApplicationEvent(final AuthenticationFailureBadCredentialsEvent e) {
        loginAttemptWatcher.loginFailed(e.getAuthentication().getPrincipal().toString());
        backendThrottler.newFailedLogin();
        backendThrottler.activateIfNeeded();
    }
}
