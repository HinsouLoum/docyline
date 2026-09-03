package com.laboacamedy.docyline.config;

import com.laboacamedy.docyline.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuration centrale de la securite de l'application.
 * Definit les routes publiques/protegees, la politique JWT (stateless) et les regles CORS,
 * conformement a la matrice des droits d'acces du cahier des charges.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth-> auth
//                Routes publiques: authentification, catalogue en consultation, documentation
                                .requestMatchers("/api/auth/**","/swagger-ui/**","/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/concours/**","/api/matieres/**",
                                        "/api/categories/**","/api/documents/","/api/documents/rechercher").permitAll()
//                                Gestion du contenu : reservee a l'admin et au gestionnaire
                                .requestMatchers(HttpMethod.POST,"/api/concours/**", "/api/matieres/**",
                                        "/api/categories/**","/api/documents/**","/api/quiz/**").hasAnyRole("ADMINISTRATEUR","GESTIONNAIRE")
                                .requestMatchers(HttpMethod.PUT,"/api/concours/**", "/api/concours/**","/api/matieres/**",
                                        "/api/categoriess/**","/api/documents/**","/api/quiz/**").hasAnyRole("ADMINISTRATEUR","GESTIONNAIRE")
                                .requestMatchers(HttpMethod.DELETE, "/api/concours/**","/api/matieres/**",
                                        "/api/categories/**","/api/documents/**","/api/quiz/**").hasAnyRole("ADMINISTRATEUR","GESTIONNAIRE")
//
//                                Statistiques et administration des comptes : admin uniquement
                                .requestMatchers("/api/statistiques/**","/api/admin/**").hasRole("ADMINISTRATEUR")

//                        Panier, commandes, paiements, quiz, telechargements: candidats connectes
                                .requestMatchers("/api/paier/**","/api/commandes/**","/api/paiements/**",
                                        "/api/telechargements/**","/api/quiz/passer/**","/api/resultats/**").authenticated()

//                        Toute autre route necessite d'etre authentifie
                                .anyRequest().authenticated()
                )
//                API sans etat : aucune session HTTP cote serveure, tout repose sur le JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return  provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

// Chiffrement des mots de passe avec BCrypt (besoin non fonctionnel : securite)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    Autorise le frontend Angular (localhost:4200) a appeler l'API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}
