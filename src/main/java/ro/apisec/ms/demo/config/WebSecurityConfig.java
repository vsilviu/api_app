package ro.apisec.ms.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private CustomAuthenticationSuccessHandler customHandler;
//
//    @Autowired
//    private CustomAuthenticationFailureHandler failureHandler;

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
//                .successHandler(customHandler)
//                .failureHandler(failureHandler)
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
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
}
