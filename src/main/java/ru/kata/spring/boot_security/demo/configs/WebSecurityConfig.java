package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService; // сервис, который отвечает за предоставление пользователя
    private final SuccessUserHandler successUserHandler;
    public WebSecurityConfig(@Qualifier("userDetailsServiceImpl")
                                  UserDetailsService userDetailsService,
                          SuccessUserHandler successUserHandler) {
        this.userDetailsService = userDetailsService;
        this.successUserHandler = successUserHandler;
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll() // доступность всем
                .antMatchers("/user").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/admin").hasAuthority("ADMIN")
                .antMatchers("/edit").hasAuthority("ADMIN")
                .antMatchers("/add").hasAuthority("ADMIN")
                .and().formLogin()  // Spring сам подставит свою логин форму
                .successHandler(successUserHandler);  // SuccessHandler перенаправляет по ролям

    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}