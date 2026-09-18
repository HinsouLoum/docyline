package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.AuthResponse;
import com.laboacamedy.docyline.dto.ConnexionRequest;
import com.laboacamedy.docyline.dto.InscriptionResquest;
import com.laboacamedy.docyline.entities.Utilisateur;
import com.laboacamedy.docyline.entities.enums.Role;
import com.laboacamedy.docyline.entities.enums.StatutCompte;
import com.laboacamedy.docyline.exception.RequeteInvalideException;
import com.laboacamedy.docyline.repository.UtilisateurRepository;
import com.laboacamedy.docyline.security.CustomUserDetails;
import com.laboacamedy.docyline.security.JwtService;
import com.laboacamedy.docyline.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementation du service d'authentification.
 * Gere la creation de compte candidat et la connexion avec generation de token JWT.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl  implements AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthResponse inscrire(InscriptionResquest requete) {
//        Verifie que l'email n'est pas deja utilise (contrainte d'unicite metier)
        if (utilisateurRepository.existsByEmail(requete.getEmail())){
            throw new RequeteInvalideException("Un compte existe deja avec cet email");
        }
        Utilisateur utilisateur = Utilisateur.builder()
                .nom(requete.getNom())
                .prenom(requete.getPrenom())
                .email(requete.getEmail())
                .telephone(requete.getTelephone())
//                Le mot de passe est chiffre avant d'etre enregistre (jamais stocke en clair)
                .motDePasse(passwordEncoder.encode(requete.getMotDePasse()))
                .role(Role.CANDIDAT) // Toute inscription pulbique cree un compte "Candidat"
                .statut(StatutCompte.ACTIF)
                .build();

        utilisateur = utilisateurRepository.save(utilisateur);
        CustomUserDetails userDetails = new  CustomUserDetails(utilisateur);
        String token = jwtService.genererToken(userDetails, utilisateur.getRole().name());
        return construireReponse(utilisateur,token);
    }


    @Override
    public AuthResponse connecter(ConnexionRequest requete) {
//        Delegue la verification email/mot de passe a Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requete.getEmail(),requete.getMotDePasse()));

        Utilisateur utilisateur = utilisateurRepository.findByEmail(requete.getEmail())
                .orElseThrow(()->new RequeteInvalideException("Utilisateur introuvable"));

        utilisateur.setDateDerniereConnexion(LocalDateTime.now());
        utilisateurRepository.save(utilisateur);

        CustomUserDetails userDetails = new CustomUserDetails(utilisateur);
        String token = jwtService.genererToken(userDetails,utilisateur.getRole().name());

        return construireReponse(utilisateur,token);
    }

    private AuthResponse construireReponse(Utilisateur utilisateur, String token) {
        return AuthResponse.builder()
                .token(token)
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .email(utilisateur.getEmail())
                .role(utilisateur.getRole().name())
                .build();
    }
}
