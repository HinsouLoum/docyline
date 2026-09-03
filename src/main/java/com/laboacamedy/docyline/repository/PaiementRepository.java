package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    Optional<Paiement> findByReference(String reference);
    Optional<Paiement> findByCommandeId(Long commandeId);
}
