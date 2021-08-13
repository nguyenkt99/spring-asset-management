package com.nashtech.rookies.AssetManagement.security.service;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nashtech.rookies.AssetManagement.model.entity.Account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AccountDetailImpl implements UserDetails{

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private boolean isFirstLogin;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public AccountDetailImpl(Long id, String username, String password, boolean isFirstLogin,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isFirstLogin = isFirstLogin;
        this.authorities = authorities;
    }

    public static AccountDetailImpl build(Account account) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(account.getRole().getName().toString()));
        return new AccountDetailImpl(
            account.getAccountId(),
            account.getUsername(),
            account.getPassword(),
            account.isFirstLogin(),
            authorities);
    }

    public Long getId() {
        return id;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AccountDetailImpl user = (AccountDetailImpl) o;
        return Objects.equals(id, user.id);        
    }
    
}
