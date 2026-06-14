package com.money.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.money.entity.ProfileEntity;
import com.money.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    /**
     * In-request cache: stores the loaded profile for the duration of the
     * current thread (HTTP request). This prevents Spring Security from
     * issuing repeated SELECT queries when multiple filters or the
     * DaoAuthenticationProvider also call loadUserByUsername.
     *
     * ThreadLocal is cleared at the end of every request by JwtRequestFilter.
     */
    private final ThreadLocal<Map<String, ProfileEntity>> requestCache =
            ThreadLocal.withInitial(HashMap::new);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Return cached profile if already loaded in this request
        ProfileEntity cached = requestCache.get().get(email);
        if (cached != null) {
            return cached;
        }

        ProfileEntity profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Profile not found with email: " + email));

        requestCache.get().put(email, profile);
        return profile;
    }

    /** Called by JwtRequestFilter after the request completes to avoid memory leaks. */
    public void clearRequestCache() {
        requestCache.remove();
    }
}
