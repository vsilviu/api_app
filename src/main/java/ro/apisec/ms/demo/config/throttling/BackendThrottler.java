package ro.apisec.ms.demo.config.throttling;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Validated
@ConfigurationProperties("backend.throttler")
public class BackendThrottler {

    @NotNull
    private Integer minutes;

    @NotNull
    private Integer maxFailedLogins;

    private static final List<LocalDateTime> failedLogins = new ArrayList<>();

    public void newFailedLogin() {
        failedLogins.add(LocalDateTime.now());
    }

    public void activateIfNeeded() {
        if (reachedLimit()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean reachedLimit() {
        return reached(maxFailedLogins);
    }

    public boolean reached(final Integer failedLoginNr) {
        final LocalDateTime now = LocalDateTime.now();
        int failedLoginsInPeriod = failedLogins
                .stream()
                .filter(time -> failedLoginInSearchedInterval(now, time))
                .collect(Collectors.toList()).size();
        return failedLoginsInPeriod > failedLoginNr;
    }

    private boolean failedLoginInSearchedInterval(final LocalDateTime now, final LocalDateTime time) {
        return time.plusMinutes(minutes).isAfter(now);
    }

    public void setMinutes(final Integer minutes) {
        this.minutes = minutes;
    }

    public void setMaxFailedLogins(final Integer maxFailedLogins) {
        this.maxFailedLogins = maxFailedLogins;
    }
}
