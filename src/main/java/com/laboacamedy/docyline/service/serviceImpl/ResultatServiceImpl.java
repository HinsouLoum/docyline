package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.ResultatRequest;
import com.laboacamedy.docyline.dto.ResultatResponse;
import com.laboacamedy.docyline.entities.Quiz;
import com.laboacamedy.docyline.entities.Resultat;
import com.laboacamedy.docyline.entities.Utilisateur;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.QuizRepository;
import com.laboacamedy.docyline.repository.ResultatRepository;
import com.laboacamedy.docyline.repository.UtilisateurRepository;
import com.laboacamedy.docyline.service.ResultatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/** Implementation du service de gestion des resultats. */
@Service
@RequiredArgsConstructor
@Transactional
public class ResultatServiceImpl implements ResultatService {

    private final ResultatRepository resultatRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final QuizRepository quizRepository;

    @Override
    public ResultatResponse creer(ResultatRequest requete) {
        Utilisateur candidat = utilisateurRepository.findById(requete.getCandidatId())
                .orElseThrow(() -> new RessourceNonTrouveeException("Candidat introuvable avec l'id : " + requete.getCandidatId()));

        Quiz quiz = quizRepository.findById(requete.getQuizId())
                .orElseThrow(() -> new RessourceNonTrouveeException("Quiz introuvable avec l'id : " + requete.getQuizId()));

        double pourcentage = (double) requete.getScore() / requete.getScoreMax() * 100;

        Resultat resultat = Resultat.builder()
                .score(requete.getScore())
                .scoreMax(requete.getScoreMax())
                .pourcentage(pourcentage)
                .candidat(candidat)
                .quiz(quiz)
                .build();

        Resultat resultatSauvegarde = resultatRepository.save(resultat);
        return mapperVersResponse(resultatSauvegarde);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultatResponse obtenirParId(Long id) {
        Resultat resultat = resultatRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Resultat introuvable avec l'id : " + id));
        return mapperVersResponse(resultat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultatResponse> listerParCandidat(Long candidatId) {
        if (!utilisateurRepository.existsById(candidatId)) {
            throw new RessourceNonTrouveeException("Candidat introuvable avec l'id : " + candidatId);
        }
        return resultatRepository.findAll().stream()
                .filter(r -> r.getCandidat().getId().equals(candidatId))
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultatResponse> listerParQuiz(Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new RessourceNonTrouveeException("Quiz introuvable avec l'id : " + quizId);
        }
        return resultatRepository.findAll().stream()
                .filter(r -> r.getQuiz().getId().equals(quizId))
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultatResponse> listerToutes() {
        return resultatRepository.findAll().stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void supprimer(Long id) {
        Resultat resultat = resultatRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Resultat introuvable avec l'id : " + id));
        resultatRepository.delete(resultat);
    }

    private ResultatResponse mapperVersResponse(Resultat resultat) {
        return ResultatResponse.builder()
                .id(resultat.getId())
                .score(resultat.getScore())
                .scoreMax(resultat.getScoreMax())
                .pourcentage(resultat.getPourcentage())
                .date(resultat.getDate())
                .candidatId(resultat.getCandidat().getId())
                .quizId(resultat.getQuiz().getId())
                .build();
    }
}
