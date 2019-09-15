package ro.apisec.ms.demo.config;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

class RandomLengthResponseInterceptor extends HandlerInterceptorAdapter {

    private static final String X_BREACH_PADDING = "X-BREACH-Padding";

    private static final int MAX_LENGTH = 128;

    private static final int MIN_LENGTH = 16;

    private final List<String> paddings = new ArrayList<>();

    private final Random random = new Random();

    RandomLengthResponseInterceptor() {
        IntStream.range(MIN_LENGTH, MAX_LENGTH)
                .forEach(length -> paddings.add(RandomStringUtils.randomAlphanumeric(length)));
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response, final Object handler) {
        response.setHeader(X_BREACH_PADDING, paddings.get(random.nextInt(paddings.size())));
        return true;
    }
}
