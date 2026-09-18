package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.StatistiquesAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Repository pour la gestion des statistiques d'achat. */
@Repository
public interface StatistiquesAchatRepository extends JpaRepository<StatistiquesAchat,Long> {
    Optional<StatistiquesAchat> findByUtilisateurId(Long utilisateurId);

    @Query("SELECT s FROM StatistiquesAchat s ORDER BY s.montantTotalDepense DESC LIMIT :limit")
    List<StatistiquesAchat> findTopMeilleurClients(@Param("limit") int limit);

    @Query("SELECT s FROM StatistiquesAchat s ORDER BY s.nombreDocumentsAchetes DESC LIMIT :limit")
    List<StatistiquesAchat> findTopPlusAcheteurs(@Param("limit") int limit);

    @Query("SELECT s FROM StatistiquesAchat s ORDER BY s.nombreTelechargements DESC LIMIT :limit")
    List<StatistiquesAchat> findTopPlusTelecharges(@Param("limit") int limit);
}
