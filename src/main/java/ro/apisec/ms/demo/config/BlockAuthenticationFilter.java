package ro.apisec.ms.demo.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ro.apisec.ms.demo.config.throttling.BackendThrottler;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class BlockAuthenticationFilter implements Filter {

    private final BackendThrottler backendThrottler;

    private final Integer blockAuthenticationFailedLogin;

    @Autowired
    public BlockAuthenticationFilter(final BackendThrottler backendThrottler,
                                     @Value("${block.authentication.failed.login}") Integer blockAuthenticationFailedLogin) {
        this.backendThrottler = Objects.requireNonNull(backendThrottler, "backendThrottler must not be null.");
        this.blockAuthenticationFailedLogin =
                Objects.requireNonNull(blockAuthenticationFailedLogin, "blockAuthenticationFailedLogins must not be null.");
    }


    @Override
    public void init(final FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (backendThrottler.reached(blockAuthenticationFailedLogin)) {
            final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            if (!httpServletRequest.getRequestURI().endsWith("/loginDisabled")) {
                httpServletResponse.sendRedirect("/loginDisabled");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
