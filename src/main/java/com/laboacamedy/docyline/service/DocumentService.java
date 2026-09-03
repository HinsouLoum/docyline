package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.DocumentRequest;
import com.laboacamedy.docyline.dto.DocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/** Interface du service de gestion des documents numeriques. */
public interface DocumentService {

    DocumentResponse ajouter(DocumentRequest request, MultipartFile fichierPdf, MultipartFile image);
    DocumentResponse modifier(Long id, DocumentRequest requete);
    void changerStatut(Long id, boolean disponible);
    DocumentResponse obtenirParId(Long id);
    List<DocumentResponse> listerCatalogue();
    List<DocumentResponse> rechercher(String motCle);
    List<DocumentResponse> filtrerParConcours(Long concoursId);
    List<DocumentResponse> filtrerParMatiere(Long categorieId);
    List<DocumentResponse> filtrerParCategorie(Long categorieId);
    List<DocumentResponse> documentsLesPlusVendus();
}
