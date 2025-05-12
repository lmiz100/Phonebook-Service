package io.github.lmizrahi.phonebook.configurations;

import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration(proxyBeanMethods = false)
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange((exchange) -> {
                    exchange.matchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
                    exchange.pathMatchers("/api/contacts/**").permitAll();
                    exchange.pathMatchers("/actuator/**").permitAll();
                });
        http.formLogin(withDefaults());
        return http.build();
    }
}
