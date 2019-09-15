package ro.apisec.ms.demo.config.throttling;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class LoginAttemptWatcher {

    private LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptWatcher() {
        super();
        attemptsCache = CacheBuilder
                .newBuilder().
                        expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<String, Integer>() {
                    public Integer load(final String key) {
                        return 0;
                    }
                });
    }

    void loginSucceeded(final String key) {
        attemptsCache.invalidate(key);
    }

    void loginFailed(final String key) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(final String key) {
        try {
            int MAX_ATTEMPT = 10;
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (final ExecutionException e) {
            return false;
        }
    }
}