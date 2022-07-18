package me.snowlight.springcloudusers.config;

import javax.servlet.Filter;

import org.bouncycastle.jcajce.provider.keystore.BC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import me.snowlight.springcloudusers.service.UserService;

// TODO Deplication 
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        // http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests().antMatchers("/**")
            .hasIpAddress("192.168.0.5")
            .and().addFilter(getAuthentionFilter());
        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthentionFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager());
        return authenticationFilter;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
    
    

    // @Bean
    // public WebSecurityCustomizer ignoringCustomizer() {
    //     return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    // }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    //     HttpSecurity disable = httpSecurity.csrf().disable();
    //     httpSecurity.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
    //     return httpSecurity.build();
    // }

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http.authorizeRequests().antMatchers("/**").hasRole("USER").and().formLogin();
    // }

}
