package com.wa.mse.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint((request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilterAfter(new JWTAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, jwtConfig.getURI()).permitAll()
                .antMatchers("/comments/admin/**").hasRole("ADMIN")
                .antMatchers("/categories/admin/**").hasRole("ADMIN")
                .antMatchers("/adventures/admin/**").hasRole("ADMIN")
                .antMatchers("/adventures/sessions/admin/**").hasRole("ADMIN")
                .antMatchers("/users/admin/**").hasRole("ADMIN")
                //.antMatchers("/users/**").hasRole("USER")
                .antMatchers("/images/admin/**").hasRole("ADMIN")
                .antMatchers("/images/adventure/admin/**").hasRole("ADMIN")
                .antMatchers("/images/category/admin/**").hasRole("ADMIN")
                .antMatchers("/images/user/admin/**").hasRole("ADMIN")
                .antMatchers("/orders/admin/**").hasRole("ADMIN")
                .antMatchers("/orders/demands/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll();
        // TODO: Demande du token pour les actions utilisateurs et cie
    }

    @Bean
    public JWTConfig jwtConfig() {
        return new JWTConfig();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("PATCH");
        corsConfiguration.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", corsConfiguration.applyPermitDefaultValues());
        return source;
    }
}
