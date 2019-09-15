package ro.apisec.ms.demo.config.throttling;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
public class LoginAttemptFilter implements Filter {

    @Autowired
    private LoginAttemptWatcher watcher;

    @Override
    public void init(final FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (isBasicAuthCall(req)) {
            String header = ((HttpServletRequest) request).getHeader("authorization");
            if (StringUtils.isNotEmpty(header)) {
                String decoded = new String(Base64.getDecoder().decode(header.split(" ")[1]), "UTF-8");
                if (watcher.isBlocked(decoded.split(":")[0])) {
                    HttpServletResponse rs = (HttpServletResponse) response;
                    rs.setHeader("authorization", "");
                    rs.sendRedirect("/");

                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isBasicAuthCall(final HttpServletRequest req) {
        return req.getRequestURI().startsWith("/basicAuthEndpoint");
    }

    @Override
    public void destroy() {

    }
}
