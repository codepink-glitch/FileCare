package ru.codepink_glitch.filecare.configuration;

import ru.codepink_glitch.filecare.filters.AuthenticationFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration
 * pretty self-explanatory
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {

    AuthenticationFilter authenticationFilter;

    // Configuring endpoints for non-authenticated requests
    // Adding token verifying filter
    // And adding cors for frontend requests to proceed

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/authentication").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/registration/**").permitAll()
                // TODO delete this line and disable h2 console
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Password encoding bean

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // idk why it's verifying token two times so i disable one of it

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> jwtRequestFilterRegistration(AuthenticationFilter authenticationFilter) {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>(authenticationFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }


}
