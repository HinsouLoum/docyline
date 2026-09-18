package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Commande;
import com.laboacamedy.docyline.entities.enums.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Repository pour la gestion des commandes. */
@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    Optional<Commande> findByNumero(String numero);
    List<Commande> findByUtilisateurId(Long utilisateurId);
    List<Commande> findByStatut(StatutCommande statut);
    boolean existsByNumero(String numero);
}
