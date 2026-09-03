package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.AuthResponse;
import com.laboacamedy.docyline.dto.ConnexionRequest;
import com.laboacamedy.docyline.dto.InscriptionResquest;

/**
 * Interface du service d'authentification :
 * inscription des candidats, connexion, et generation du token JWT.
 */
public interface AuthService {
    AuthResponse inscrire(InscriptionResquest requete);
    AuthResponse connecter(ConnexionRequest requete);
}
