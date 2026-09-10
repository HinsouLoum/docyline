package com.laboacamedy.docyline.security;

import com.laboacamedy.docyline.entities.Utilisateur;
import com.laboacamedy.docyline.entities.enums.StatutCompte;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Adaptateur entre l'entite Utilisateur et l'interface UserDetails requise par Spring Security.
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Utilisateur utilisateur;

    public Utilisateur getUtilisateur(){
        return utilisateur;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Le role est prefixe par "ROLE" pour etre compatible avec hasRole() de Spring Security
        return List.of(new SimpleGrantedAuthority("ROLE"+utilisateur.getRole().name()));
    }

    @Override
    public @Nullable String getPassword() {
        return utilisateur.getMotDePasse();
    }

    @Override
    public String getUsername() {
        return utilisateur.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return utilisateur.getStatut() == StatutCompte.ACTIF;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return utilisateur.getStatut() ==StatutCompte.ACTIF;
    }
}
