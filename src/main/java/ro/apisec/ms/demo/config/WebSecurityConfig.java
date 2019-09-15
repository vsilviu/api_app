package ro.apisec.ms.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import ro.apisec.ms.demo.config.throttling.LoginAttemptFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomAuthenticationSuccessHandler customHandler;

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.
                authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/user/**").hasAuthority("USER")
                .antMatchers("/static/**", "/login", "/css/**", "/webjars/**", "/loginDisabled").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login").permitAll()
                .successHandler(customHandler)
                .failureHandler(failureHandler)
        ;
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder amb) throws Exception {
        amb
                .inMemoryAuthentication()
                .withUser("user")
                .password("$2a$10$KJhOzmLH8f51SVbuPVAq4OloN4eYnI7tYzwbOkb43SUr2XJ0IrucS").authorities("USER")
                .and()
                .withUser("admin")
                .password("$2a$10$KJhOzmLH8f51SVbuPVAq4OloN4eYnI7tYzwbOkb43SUr2XJ0IrucS").authorities("ADMIN")
                .and()
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Configuration
    @Order(1)
    public static class BasicAuthentication extends WebSecurityConfigurerAdapter {

        private static final String ROLE_SPECIFIC_FOR_ENDPOINT = "BASIC_AUTH";

        @Autowired
        private LoginAttemptFilter filter;

        @Value("${basic.auth.username}")
        private String username;

        @Value("${basic.auth.password}")
        private String password;

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http.addFilterBefore(filter, BasicAuthenticationFilter.class);
            http
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .antMatcher("/basicAuthEndpoint")
                    .authorizeRequests()
                    .anyRequest()
                    .hasAuthority(ROLE_SPECIFIC_FOR_ENDPOINT)
                    .and()
                    .httpBasic()
            ;
        }

        @Override
        protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .inMemoryAuthentication()
                    .withUser(username)
                    .password(password)
                    .authorities(ROLE_SPECIFIC_FOR_ENDPOINT);
        }

        @Bean
        public AuthenticationFailureHandler authenticationFailureHandler() {
            return new SimpleUrlAuthenticationFailureHandler();
        }
    }
}
