package com.laboacamedy.docyline.security;

import com.laboacamedy.docyline.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Service utilise par Spring Security pour charger un utilisateur a partir de son email. */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return utilisateurRepository.findByEmail(email)
                .map(CustomUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("Aucun utilisateur trouve avec l'email: " + email));
    }
}
