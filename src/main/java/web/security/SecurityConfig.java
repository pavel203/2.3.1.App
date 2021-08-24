package web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import web.security.handler.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableJpaRepositories("web")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private  UserDetailsService userDetailsService;

    @Autowired
    private  LoginSuccessHandler successUserHandler;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                     .antMatchers("/login").anonymous()
                     .antMatchers("/admin").access("hasAnyRole('ADMIN')")
                     .antMatchers("/admin/new").access("hasAnyRole('ADMIN')")
                     .antMatchers("/admin/{id}/edit").access("hasAnyRole('ADMIN')")
                     .antMatchers("/admin/{id}/delete").access("hasAnyRole('ADMIN')")
                .anyRequest().authenticated()
                .and().formLogin()
                .successHandler(successUserHandler);
    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}
