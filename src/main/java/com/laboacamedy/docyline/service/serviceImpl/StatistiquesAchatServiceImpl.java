package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.DashboardUtilisateurResponse;
import com.laboacamedy.docyline.dto.StatistiquesAchatResponse;
import com.laboacamedy.docyline.entities.StatistiquesAchat;
import com.laboacamedy.docyline.entities.Utilisateur;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.*;
import com.laboacamedy.docyline.service.StatistiquesAchatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** Implementation du service de gestion des statistiques d'achat. */
@Service
@RequiredArgsConstructor
@Transactional
public class StatistiquesAchatServiceImpl implements StatistiquesAchatService {

    private final StatistiquesAchatRepository statistiquesRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final BibliothequeUtilisateurRepository bibliothequeRepository;
    private final ResultatRepository resultatRepository;
    private final QuizRepository quizRepository;

    @Override
    public StatistiquesAchatResponse creerOuObtenirPourUtilisateur(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Utilisateur introuvable avec l'id : " + utilisateurId));

        StatistiquesAchat statistiques = statistiquesRepository.findByUtilisateurId(utilisateurId)
                .orElseGet(() -> {
                    StatistiquesAchat nouvellesStats = StatistiquesAchat.builder()
                            .utilisateur(utilisateur)
                            .build();
                    return statistiquesRepository.save(nouvellesStats);
                });

        return mapperVersResponse(statistiques);
    }

    @Override
    @Transactional(readOnly = true)
    public StatistiquesAchatResponse obtenirParId(Long id) {
        StatistiquesAchat statistiques = statistiquesRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Statistiques introuvables avec l'id : " + id));
        return mapperVersResponse(statistiques);
    }

    @Override
    @Transactional(readOnly = true)
    public StatistiquesAchatResponse obtenirParUtilisateur(Long utilisateurId) {
        StatistiquesAchat statistiques = statistiquesRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Aucune statistique pour l'utilisateur : " + utilisateurId));
        return mapperVersResponse(statistiques);
    }

    @Override
    public StatistiquesAchatResponse incrementerDocumentsAchetes(Long utilisateurId, BigDecimal montant) {
        StatistiquesAchat statistiques = statistiquesRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Statistiques introuvables pour l'utilisateur : " + utilisateurId));

        statistiques.setNombreDocumentsAchetes(statistiques.getNombreDocumentsAchetes() + 1);
        statistiques.setMontantTotalDepense(statistiques.getMontantTotalDepense().add(montant));

        if (statistiques.getDatePremiereCommande() == null) {
            statistiques.setDatePremiereCommande(LocalDateTime.now());
        }

        StatistiquesAchat statistiquesModifiees = statistiquesRepository.save(statistiques);
        return mapperVersResponse(statistiquesModifiees);
    }

    @Override
    public StatistiquesAchatResponse incrementerTelechargements(Long utilisateurId) {
        StatistiquesAchat statistiques = statistiquesRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Statistiques introuvables pour l'utilisateur : " + utilisateurId));

        statistiques.setNombreTelechargements(statistiques.getNombreTelechargements() + 1);
        statistiques.setDerniereDateTelechargement(LocalDateTime.now());

        StatistiquesAchat statistiquesModifiees = statistiquesRepository.save(statistiques);
        return mapperVersResponse(statistiquesModifiees);
    }

    @Override
    public StatistiquesAchatResponse incrementerCommandes(Long utilisateurId) {
        StatistiquesAchat statistiques = statistiquesRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Statistiques introuvables pour l'utilisateur : " + utilisateurId));

        statistiques.setNombreCommandes(statistiques.getNombreCommandes() + 1);

        StatistiquesAchat statistiquesModifiees = statistiquesRepository.save(statistiques);
        return mapperVersResponse(statistiquesModifiees);
    }

    @Override
    public StatistiquesAchatResponse mettreAJourScoreQuiz(Long utilisateurId, Integer score, Integer scoreMax) {
        StatistiquesAchat statistiques = statistiquesRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Statistiques introuvables pour l'utilisateur : " + utilisateurId));

        // Calculer la nouvelle moyenne
        Integer totalQuiz = statistiques.getNombreQuizPasses() + 1;
        BigDecimal scorePercentage = BigDecimal.valueOf((double) score / scoreMax * 100);

        BigDecimal ancienneMoyenne = statistiques.getScoreMoyenQuiz() != null
                ? statistiques.getScoreMoyenQuiz()
                : BigDecimal.ZERO;

        BigDecimal nouvelleMoyenne = ancienneMoyenne
                .multiply(BigDecimal.valueOf(statistiques.getNombreQuizPasses()))
                .add(scorePercentage)
                .divide(BigDecimal.valueOf(totalQuiz), 2, java.math.RoundingMode.HALF_UP);

        statistiques.setScoreMoyenQuiz(nouvelleMoyenne);
        statistiques.setNombreQuizPasses(totalQuiz);

        StatistiquesAchat statistiquesModifiees = statistiquesRepository.save(statistiques);
        return mapperVersResponse(statistiquesModifiees);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatistiquesAchatResponse> obtenirTopMeilleurClients(int limit) {
        return statistiquesRepository.findTopMeilleurClients(limit).stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatistiquesAchatResponse> obtenirTopPlusAcheteurs(int limit) {
        return statistiquesRepository.findTopPlusAcheteurs(limit).stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatistiquesAchatResponse> obtenirTopPlusTelecharges(int limit) {
        return statistiquesRepository.findTopPlusTelecharges(limit).stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardUtilisateurResponse obtenirDashboardUtilisateur(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Utilisateur introuvable avec l'id : " + utilisateurId));

        StatistiquesAchat stats = statistiquesRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Statistiques introuvables pour l'utilisateur : " + utilisateurId));

        Integer documentsEnBibliotheque = bibliothequeRepository.findByUtilisateurId(utilisateurId)
                .map(b -> b.getDocuments().size())
                .orElse(0);

        Double montantMoyen = stats.getNombreCommandes() > 0
                ? stats.getMontantTotalDepense().doubleValue() / stats.getNombreCommandes()
                : 0.0;

        Double tauxTelechargement = stats.getNombreDocumentsAchetes() > 0
                ? (double) stats.getNombreTelechargements() / stats.getNombreDocumentsAchetes() * 100
                : 0.0;

        Double tauxReussite = stats.getNombreQuizPasses() > 0
                ? stats.getScoreMoyenQuiz().doubleValue()
                : 0.0;

        return DashboardUtilisateurResponse.builder()
                .utilisateurId(utilisateur.getId())
                .nomUtilisateur(utilisateur.getNom() + " " + utilisateur.getPrenom())
                .emailUtilisateur(utilisateur.getEmail())
                .nombreDocumentsAchetes(stats.getNombreDocumentsAchetes())
                .montantTotalDepense(stats.getMontantTotalDepense())
                .nombreCommandes(stats.getNombreCommandes())
                .montantMoyenParCommande(montantMoyen)
                .nombreTelechargements(stats.getNombreTelechargements())
                .tauxTelechargement(tauxTelechargement)
                .scoreMoyenQuiz(stats.getScoreMoyenQuiz())
                .nombreQuizPasses(stats.getNombreQuizPasses())
                .tauxReussite(tauxReussite)
                .documentsEnBibliotheque(documentsEnBibliotheque)
                .build();
    }

    @Override
    public void supprimer(Long id) {
        StatistiquesAchat statistiques = statistiquesRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Statistiques introuvables avec l'id : " + id));
        statistiquesRepository.delete(statistiques);
    }

    private StatistiquesAchatResponse mapperVersResponse(StatistiquesAchat statistiques) {
        Double montantMoyen = statistiques.getNombreCommandes() > 0
                ? statistiques.getMontantTotalDepense().doubleValue() / statistiques.getNombreCommandes()
                : 0.0;

        Double tauxTelechargement = statistiques.getNombreDocumentsAchetes() > 0
                ? (double) statistiques.getNombreTelechargements() / statistiques.getNombreDocumentsAchetes() * 100
                : 0.0;

        return StatistiquesAchatResponse.builder()
                .id(statistiques.getId())
                .utilisateurId(statistiques.getUtilisateur().getId())
                .nombreDocumentsAchetes(statistiques.getNombreDocumentsAchetes())
                .montantTotalDepense(statistiques.getMontantTotalDepense())
                .nombreTelechargements(statistiques.getNombreTelechargements())
                .nombreCommandes(statistiques.getNombreCommandes())
                .scoreMoyenQuiz(statistiques.getScoreMoyenQuiz())
                .nombreQuizPasses(statistiques.getNombreQuizPasses())
                .derniereDateTelechargement(statistiques.getDerniereDateTelechargement())
                .datePremiereCommande(statistiques.getDatePremiereCommande())
                .dateCreation(statistiques.getDateCreation())
                .dateModification(statistiques.getDateModification())
                .montantMoyenParCommande(montantMoyen)
                .tauxTelechargement(tauxTelechargement)
                .build();
    }
}
