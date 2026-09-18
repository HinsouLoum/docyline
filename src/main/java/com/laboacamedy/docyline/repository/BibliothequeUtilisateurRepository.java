package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.BibliothequeUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Repository pour la gestion des bibliotheques utilisateurs. */
@Repository
public interface BibliothequeUtilisateurRepository  extends JpaRepository<BibliothequeUtilisateur, Long> {
    Optional<BibliothequeUtilisateur> findByUtilisateurId(Long utilisateurId);
    boolean existsByUtilisateurId(Long utilisateurId);
}
