package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Commande;
import com.laboacamedy.docyline.entities.enums.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByUtilisateurIdOrderByDateCommandeDesc(Long UtilisateurId);
    Optional<Commande> findByNumero(String numero);
    long countByStatut(StatutCommande statutCommande);
}
