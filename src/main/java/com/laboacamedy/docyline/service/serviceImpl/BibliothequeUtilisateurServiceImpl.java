package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.BibliothequeUtilisateurResponse;
import com.laboacamedy.docyline.dto.DocumentBibliothequeResponse;
import com.laboacamedy.docyline.entities.BibliothequeUtilisateur;
import com.laboacamedy.docyline.entities.Document;
import com.laboacamedy.docyline.entities.Utilisateur;
import com.laboacamedy.docyline.exception.RequeteInvalideException;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.BibliothequeUtilisateurRepository;
import com.laboacamedy.docyline.repository.DocumentRepository;
import com.laboacamedy.docyline.repository.UtilisateurRepository;
import com.laboacamedy.docyline.service.BibliothequeUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/** Implementation du service de gestion des bibliotheques utilisateurs. */
@Service
@RequiredArgsConstructor
@Transactional
public class BibliothequeUtilisateurServiceImpl implements BibliothequeUtilisateurService {

    private final BibliothequeUtilisateurRepository bibliothequeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final DocumentRepository documentRepository;

    @Override
    public BibliothequeUtilisateurResponse creerOuObtenirPourUtilisateur(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Utilisateur introuvable avec l'id : " + utilisateurId));

        BibliothequeUtilisateur bibliotheque = bibliothequeRepository.findByUtilisateurId(utilisateurId)
                .orElseGet(() -> {
                    BibliothequeUtilisateur nouvelleBibliotheque = BibliothequeUtilisateur.builder()
                            .utilisateur(utilisateur)
                            .build();
                    return bibliothequeRepository.save(nouvelleBibliotheque);
                });

        return mapperVersResponse(bibliotheque);
    }

    @Override
    @Transactional(readOnly = true)
    public BibliothequeUtilisateurResponse obtenirParId(Long id) {
        BibliothequeUtilisateur bibliotheque = bibliothequeRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Bibliotheque introuvable avec l'id : " + id));
        return mapperVersResponse(bibliotheque);
    }

    @Override
    @Transactional(readOnly = true)
    public BibliothequeUtilisateurResponse obtenirParUtilisateur(Long utilisateurId) {
        BibliothequeUtilisateur bibliotheque = bibliothequeRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Aucune bibliotheque pour l'utilisateur : " + utilisateurId));
        return mapperVersResponse(bibliotheque);
    }

    @Override
    public BibliothequeUtilisateurResponse ajouterDocument(Long bibliothequeId, Long documentId) {
        BibliothequeUtilisateur bibliotheque = bibliothequeRepository.findById(bibliothequeId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Bibliotheque introuvable avec l'id : " + bibliothequeId));

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Document introuvable avec l'id : " + documentId));

        // Verifier que le document n'est pas deja dans la bibliotheque
        boolean exists = bibliotheque.getDocuments().stream()
                .anyMatch(d -> d.getId().equals(documentId));
        if (exists) {
            throw new RequeteInvalideException("Ce document est deja present dans la bibliotheque");
        }

        bibliotheque.getDocuments().add(document);
        BibliothequeUtilisateur bibliothequeModifiee = bibliothequeRepository.save(bibliotheque);
        return mapperVersResponse(bibliothequeModifiee);
    }

    @Override
    public BibliothequeUtilisateurResponse retirerDocument(Long bibliothequeId, Long documentId) {
        BibliothequeUtilisateur bibliotheque = bibliothequeRepository.findById(bibliothequeId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Bibliotheque introuvable avec l'id : " + bibliothequeId));

        Document document = bibliotheque.getDocuments().stream()
                .filter(d -> d.getId().equals(documentId))
                .findFirst()
                .orElseThrow(() -> new RessourceNonTrouveeException("Document introuvable dans la bibliotheque"));

        bibliotheque.getDocuments().remove(document);
        BibliothequeUtilisateur bibliothequeModifiee = bibliothequeRepository.save(bibliotheque);
        return mapperVersResponse(bibliothequeModifiee);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean verifierPossessionDocument(Long utilisateurId, Long documentId) {
        BibliothequeUtilisateur bibliotheque = bibliothequeRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Aucune bibliotheque pour l'utilisateur : " + utilisateurId));

        return bibliotheque.getDocuments().stream()
                .anyMatch(d -> d.getId().equals(documentId));
    }

    @Override
    public void supprimer(Long id) {
        BibliothequeUtilisateur bibliotheque = bibliothequeRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Bibliotheque introuvable avec l'id : " + id));
        bibliothequeRepository.delete(bibliotheque);
    }

    private BibliothequeUtilisateurResponse mapperVersResponse(BibliothequeUtilisateur bibliotheque) {
        return BibliothequeUtilisateurResponse.builder()
                .id(bibliotheque.getId())
                .utilisateurId(bibliotheque.getUtilisateur().getId())
                .dateCreation(bibliotheque.getDateCreation())
                .documents(bibliotheque.getDocuments().stream()
                        .map(this::mapperDocumentVersResponse)
                        .collect(Collectors.toList())
                )
                .nombreDocuments(bibliotheque.getDocuments().size())
                .build();
    }

    private DocumentBibliothequeResponse mapperDocumentVersResponse(Document document) {
        return DocumentBibliothequeResponse.builder()
                .id(document.getId())
                .titre(document.getTitre())
                .description(document.getDescription())
                .prix(document.getPrix())
                .build();
    }
}
