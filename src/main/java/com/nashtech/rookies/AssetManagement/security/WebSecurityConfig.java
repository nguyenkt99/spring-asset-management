package com.nashtech.rookies.AssetManagement.security;


import com.nashtech.rookies.AssetManagement.constant.RoleName;
import com.nashtech.rookies.AssetManagement.model.entity.Role;
import com.nashtech.rookies.AssetManagement.security.jwt.JwtAuthEntryPoint;
import com.nashtech.rookies.AssetManagement.security.jwt.JwtAuthTokenFilter;
import com.nashtech.rookies.AssetManagement.security.jwt.JwtUtils;
import com.nashtech.rookies.AssetManagement.security.service.AccountDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    
    private final AccountDetailService accountDeatailService;

    private final JwtAuthEntryPoint unauthorizedHandler;

    private final JwtUtils jwtUtils;

    private static final String[] PUBLIC_MATCHERS = { "/css/**", "/js/**", "/image/**", "/book/**", "/user/**", "/**" };

    @Autowired
    public WebSecurityConfig(AccountDetailService accountDeatailService,
            JwtAuthEntryPoint unauthorizedHandler, JwtUtils jwtUtils) {
        this.accountDeatailService = accountDeatailService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter(){
        return new JwtAuthTokenFilter(jwtUtils, accountDeatailService);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService((userDetailsService()));

        return authProvider;
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
                            throws Exception{
        authenticationManagerBuilder.userDetailsService(accountDeatailService).passwordEncoder(passwordEncoder());

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
                .antMatchers("/api/users/**", "/api/users**").hasRole("ADMIN")
                .antMatchers("/account/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers("/api/categories/**", "/api/categories**").hasRole("ADMIN")
                .antMatchers("/api/assets/**", "/api/assets**").hasRole("ADMIN")
                .antMatchers("/api/states/**", "/api/states**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .antMatchers("/auth/**").permitAll().anyRequest().authenticated();
                                
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}
