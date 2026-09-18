package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.CommandeRequest;
import com.laboacamedy.docyline.dto.CommandeResponse;
import com.laboacamedy.docyline.entities.Commande;
import com.laboacamedy.docyline.entities.Document;
import com.laboacamedy.docyline.entities.Utilisateur;
import com.laboacamedy.docyline.entities.enums.StatutCommande;
import com.laboacamedy.docyline.exception.RequeteInvalideException;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.CommandeRepository;
import com.laboacamedy.docyline.repository.DocumentRepository;
import com.laboacamedy.docyline.repository.UtilisateurRepository;
import com.laboacamedy.docyline.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** Implementation du service de gestion des commandes. */
@Service
@RequiredArgsConstructor
@Transactional
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final DocumentRepository documentRepository;

    @Override
    public CommandeResponse creer(CommandeRequest requete) {
        // Verifier que l'utilisateur existe
        Utilisateur utilisateur = utilisateurRepository.findById(requete.getUtilisateurId())
                .orElseThrow(() -> new RessourceNonTrouveeException("Utilisateur introuvable avec l'id : " + requete.getUtilisateurId()));

        // Verifier que les documents existent et calculer le montant total
        List<Document> documents = requete.getDocumentIds().stream()
                .map(id -> documentRepository.findById(id)
                        .orElseThrow(() -> new RessourceNonTrouveeException("Document introuvable avec l'id : " + id)))
                .collect(Collectors.toList());

        BigDecimal montantTotal = documents.stream()
                .map(Document::getPrix)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Generer un numero unique de commande
        String numero = genererNumeroCommande();

        Commande commande = Commande.builder()
                .numero(numero)
                .montantTotal(montantTotal)
                .statut(StatutCommande.EN_ATTENTE)
                .utilisateur(utilisateur)
                .documents(documents)
                .build();

        Commande commandeSauvegardee = commandeRepository.save(commande);
        return mapperVersResponse(commandeSauvegardee);
    }

    @Override
    @Transactional(readOnly = true)
    public CommandeResponse obtenirParId(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Commande introuvable avec l'id : " + id));
        return mapperVersResponse(commande);
    }

    @Override
    @Transactional(readOnly = true)
    public CommandeResponse obtenirParNumero(String numero) {
        Commande commande = commandeRepository.findByNumero(numero)
                .orElseThrow(() -> new RessourceNonTrouveeException("Commande introuvable avec le numero : " + numero));
        return mapperVersResponse(commande);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse> listerParUtilisateur(Long utilisateurId) {
        // Verifier que l'utilisateur existe
        if (!utilisateurRepository.existsById(utilisateurId)) {
            throw new RessourceNonTrouveeException("Utilisateur introuvable avec l'id : " + utilisateurId);
        }
        return commandeRepository.findByUtilisateurId(utilisateurId).stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse> listerToutes() {
        return commandeRepository.findAll().stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CommandeResponse annuler(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Commande introuvable avec l'id : " + id));

        if (commande.getStatut() == StatutCommande.PAYEE || commande.getStatut() == StatutCommande.ANNULEE) {
            throw new RequeteInvalideException("Impossible d'annuler une commande " + commande.getStatut());
        }

        commande.setStatut(StatutCommande.ANNULEE);
        Commande commandeModifiee = commandeRepository.save(commande);
        return mapperVersResponse(commandeModifiee);
    }

    @Override
    public void supprimer(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Commande introuvable avec l'id : " + id));
        commandeRepository.delete(commande);
    }

    private String genererNumeroCommande() {
        String prefix = "CMD-" + LocalDateTime.now().getYear() + "-";
        String lastNumero = commandeRepository.findAll().stream()
                .map(Commande::getNumero)
                .filter(n -> n.startsWith(prefix))
                .sorted()
                .reduce("", (a, b) -> b);

        int nextNumber = 1;
        if (!lastNumero.isEmpty()) {
            String numberPart = lastNumero.substring(prefix.length());
            nextNumber = Integer.parseInt(numberPart) + 1;
        }

        return String.format("%s%06d", prefix, nextNumber);
    }

    private CommandeResponse mapperVersResponse(Commande commande) {
        return CommandeResponse.builder()
                .id(commande.getId())
                .numero(commande.getNumero())
                .dateCommande(commande.getDateCommande())
                .montantTotal(commande.getMontantTotal())
                .statut(commande.getStatut())
                .utilisateurId(commande.getUtilisateur().getId())
                .documentIds(commande.getDocuments().stream()
                        .map(Document::getId)
                        .collect(Collectors.toList()))
                .build();
    }
}
