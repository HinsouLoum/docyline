package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.dto.DocumentResponse;
import com.laboacamedy.docyline.entities.Document;
import com.laboacamedy.docyline.enums.StatutDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

//    Rechercher par titre ou mot-cle (besoin fonction "Recherche, consultation et filtrage")
    @Query("SELECT d FROM Document d WHERE LOWER(d.titre) LIKE LOWER(CONCAT('%', :motCle, '%')) "
        + "OR LOWER(d.description) LIKE LOWER(CONCAT('%',:motCle,'%'))")
    List<Document> rechercherParMotCle(@Param("motCle") String motCle);

    List<Document> findByConcoursId(Long concoursId);

    List<Document> findByMatiereId(Long matiereId);

    List<Document> findByCategorieId(Long categorieId);

    List<Document> findByAnnee(Integer annnee);

//    Documents les plus vendus, tries par nombre de ventes decroissant (tableau de bord)
    List<Document> findTop10ByOrderByNombreVentesDesc();

    List<Document> findByStatut(StatutDocument statutDocument);

}
