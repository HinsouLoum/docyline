package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.PaiementRequest;
import com.laboacamedy.docyline.dto.PaiementResponse;
import com.laboacamedy.docyline.entities.Commande;
import com.laboacamedy.docyline.entities.Paiement;
import com.laboacamedy.docyline.entities.enums.StatutCommande;
import com.laboacamedy.docyline.entities.enums.StatutPaiement;
import com.laboacamedy.docyline.exception.RequeteInvalideException;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.CommandeRepository;
import com.laboacamedy.docyline.repository.PaiementRepository;
import com.laboacamedy.docyline.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/** Implementation du service de gestion des paiements. */
@Service
@RequiredArgsConstructor
@Transactional
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;
    private final CommandeRepository commandeRepository;

    @Override
    public PaiementResponse creer(PaiementRequest requete) {
        Commande commande = commandeRepository.findById(requete.getCommandeId())
                .orElseThrow(() -> new RessourceNonTrouveeException("Commande introuvable avec l'id : " + requete.getCommandeId()));

        // Verifier qu'il n'existe pas deja un paiement en attente pour cette commande
        if (commande.getPaiement() != null && commande.getPaiement().getStatut() == StatutPaiement.EN_ATTENTE) {
            throw new RequeteInvalideException("Un paiement en attente existe deja pour cette commande");
        }

        String reference = genererReference();

        Paiement paiement = Paiement.builder()
                .montant(requete.getMontant())
                .reference(reference)
                .mode(requete.getMode())
                .statut(StatutPaiement.EN_ATTENTE)
                .commande(commande)
                .build();

        Paiement paiementSauvegarde = paiementRepository.save(paiement);
        return mapperVersResponse(paiementSauvegarde);
    }

    @Override
    @Transactional(readOnly = true)
    public PaiementResponse obtenirParId(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Paiement introuvable avec l'id : " + id));
        return mapperVersResponse(paiement);
    }

    @Override
    @Transactional(readOnly = true)
    public PaiementResponse obtenirParReference(String reference) {
        Paiement paiement = paiementRepository.findByReference(reference)
                .orElseThrow(() -> new RessourceNonTrouveeException("Paiement introuvable avec la reference : " + reference));
        return mapperVersResponse(paiement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementResponse> listerParCommande(Long commandeId) {
        if (!commandeRepository.existsById(commandeId)) {
            throw new RessourceNonTrouveeException("Commande introuvable avec l'id : " + commandeId);
        }
        return paiementRepository.findAll().stream()
                .filter(p -> p.getCommande().getId().equals(commandeId))
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementResponse> listerToutes() {
        return paiementRepository.findAll().stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaiementResponse confirmer(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Paiement introuvable avec l'id : " + id));

        if (paiement.getStatut() != StatutPaiement.EN_ATTENTE) {
            throw new RequeteInvalideException("Seuls les paiements en attente peuvent etre confirmes");
        }

        paiement.setStatut(StatutPaiement.CONFIRME);
        paiement.getCommande().setStatut(StatutCommande.PAYEE);

        Paiement paiementConfirme = paiementRepository.save(paiement);
        commandeRepository.save(paiement.getCommande());

        return mapperVersResponse(paiementConfirme);
    }

    @Override
    public PaiementResponse rejeter(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Paiement introuvable avec l'id : " + id));

        if (paiement.getStatut() != StatutPaiement.EN_ATTENTE) {
            throw new RequeteInvalideException("Seuls les paiements en attente peuvent etre rejetes");
        }

        paiement.setStatut(StatutPaiement.ECHOUE);
        paiement.getCommande().setStatut(StatutCommande.ANNULEE);

        Paiement paiementRejete = paiementRepository.save(paiement);
        commandeRepository.save(paiement.getCommande());

        return mapperVersResponse(paiementRejete);
    }

    @Override
    public void supprimer(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Paiement introuvable avec l'id : " + id));
        paiementRepository.delete(paiement);
    }

    private String genererReference() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private PaiementResponse mapperVersResponse(Paiement paiement) {
        return PaiementResponse.builder()
                .id(paiement.getId())
                .montant(paiement.getMontant())
                .reference(paiement.getReference())
                .mode(paiement.getMode())
                .date(paiement.getDate())
                .statut(paiement.getStatut())
                .commandeId(paiement.getCommande().getId())
                .build();
    }
}
