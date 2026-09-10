package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.QuestionRequest;
import com.laboacamedy.docyline.dto.QuizRequest;
import com.laboacamedy.docyline.dto.QuizResponse;
import com.laboacamedy.docyline.dto.ReponseRequest;
import com.laboacamedy.docyline.entities.*;
import com.laboacamedy.docyline.entities.enums.StatutQuiz;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.ConcoursRepository;
import com.laboacamedy.docyline.repository.MatiereRepository;
import com.laboacamedy.docyline.repository.QuizRepository;
import com.laboacamedy.docyline.service.QuizService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation du service de gestion des quiz.
 * cf. besoins fonctionnels "Gestion des quiz et exercices".
 * La creation se fait en une seule requete imbriquee (quiz + questions + reponses),
 * plus pratique pour le gestionnaire de contenu que de multiplier les appels.
 */
@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final ConcoursRepository concoursRepository;
    private final MatiereRepository matiereRepository;

    @Override
    @Transactional
    public QuizResponse creer(QuizRequest requete) {
        Quiz quiz = Quiz.builder()
                .titre(requete.getTitre())
                .description(requete.getDescription())
                .dureeMinutes(requete.getDureeMinutes())
                .statut(StatutQuiz.BROUILLON) // Cree en brouillon ; publie explicitement ensuite
                .concours(requete.getConcoursId() != null ? trouverConcours(requete.getConcoursId()) : null)
                .matiere(requete.getMatiereId() != null ? trouverMatiere(requete.getMatiereId()) : null)
                .build();

        construireQuestions(quiz, requete.getQuestions());

        return versDtoDetail(quizRepository.save(quiz));
    }

    @Override
    @Transactional
    public QuizResponse modifier(Long id, QuizRequest requete) {
        Quiz quiz = trouverQuiz(id);
        quiz.setTitre(requete.getTitre());
        quiz.setDescription(requete.getDescription());
        quiz.setDureeMinutes(requete.getDureeMinutes());
        if (requete.getConcoursId() != null) quiz.setConcours(trouverConcours(requete.getConcoursId()));
        if (requete.getMatiereId() != null) quiz.setMatiere(trouverMatiere(requete.getMatiereId()));

        // Remplace entierement les questions existantes par les nouvelles (orphanRemoval supprime les anciennes)
        quiz.getQuestions().clear();
        construireQuestions(quiz, requete.getQuestions());

        return versDtoDetail(quizRepository.save(quiz));
    }

    @Override
    @Transactional
    public void changerStatut(Long id, StatutQuiz statut) {
        Quiz quiz = trouverQuiz(id);
        quiz.setStatut(statut);
        quizRepository.save(quiz);
    }

    @Override
    public QuizResponse obtenirParId(Long id) {
        return versDtoDetail(trouverQuiz(id));
    }

    @Override
    public List<QuizResponse> listerTous() {
        return quizRepository.findAll().stream().map(this::versDtoResume).collect(Collectors.toList());
    }

    @Override
    public List<QuizResponse> listerPublies() {
        return quizRepository.findByStatut(StatutQuiz.PUBLIE).stream()
                .map(this::versDtoResume).collect(Collectors.toList());
    }

    @Override
    public List<QuizResponse> listerParConcours(Long concoursId) {
        return quizRepository.findByConcoursId(concoursId).stream()
                .map(this::versDtoResume).collect(Collectors.toList());
    }

    @Override
    public List<QuizResponse> listerParMatiere(Long matiereId) {
        return quizRepository.findByMatiereId(matiereId).stream()
                .map(this::versDtoResume).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void supprimer(Long id) {
        Quiz quiz = trouverQuiz(id);
        quizRepository.delete(quiz);
    }

    // --- Methodes utilitaires privees ---

    // Construit les entites Question/Reponse a partir du DTO et les rattache au quiz
    private void construireQuestions(Quiz quiz, List<QuestionRequest> questionsRequest) {
        if (questionsRequest == null) return;

        List<Question> questions = new ArrayList<>();
        for (QuestionRequest qr : questionsRequest) {
            Question question = Question.builder()
                    .contenu(qr.getContenu())
                    .points(qr.getPoints())
                    .quiz(quiz)
                    .build();

            List<Reponse> reponses = new ArrayList<>();
            for (ReponseRequest rr : qr.getReponses()) {
                reponses.add(Reponse.builder()
                        .contenu(rr.getContenu())
                        .estCorrecte(Boolean.TRUE.equals(rr.getEstCorrecte()))
                        .question(question)
                        .build());
            }
            question.setReponses(reponses);
            questions.add(question);
        }
        quiz.setQuestions(questions);
    }

    private Quiz trouverQuiz(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Quiz introuvable avec l'id : " + id));
    }

    private Concours trouverConcours(Long id) {
        return concoursRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Concours introuvable avec l'id : " + id));
    }

    private Matiere trouverMatiere(Long id) {
        return matiereRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Matiere introuvable avec l'id : " + id));
    }

    // Vue "resume" (sans le detail des questions) pour les listes
    private QuizResponse versDtoResume(Quiz quiz) {
        return QuizResponse.builder()
                .id(quiz.getId())
                .titre(quiz.getTitre())
                .description(quiz.getDescription())
                .dureeMinutes(quiz.getDureeMinutes())
                .statut(quiz.getStatut())
                .nomConcours(quiz.getConcours() != null ? quiz.getConcours().getNom() : null)
                .nomMatiere(quiz.getMatiere() != null ? quiz.getMatiere().getIntitule() : null)
                .nombreQuestions(quiz.getQuestions() != null ? quiz.getQuestions().size() : 0)
                .questions(Collections.emptyList())
                .build();
    }

    // Vue "detail" (avec questions + reponses) pour la consultation d'un quiz precis
    private QuizResponse versDtoDetail(Quiz quiz) {
        List<QuizResponse.QuestionResponse> questionsDto = quiz.getQuestions().stream()
                .map(q -> QuizResponse.QuestionResponse.builder()
                        .id(q.getId())
                        .contenu(q.getContenu())
                        .points(q.getPoints())
                        .reponses(q.getReponses().stream()
                                .map(r -> QuizResponse.ReponseResponse.builder()
                                        .id(r.getId())
                                        .contenu(r.getContenu())
                                        .estCorrecte(r.getEstCorrecte())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return QuizResponse.builder()
                .id(quiz.getId())
                .titre(quiz.getTitre())
                .description(quiz.getDescription())
                .dureeMinutes(quiz.getDureeMinutes())
                .statut(quiz.getStatut())
                .nomConcours(quiz.getConcours() != null ? quiz.getConcours().getNom() : null)
                .nomMatiere(quiz.getMatiere() != null ? quiz.getMatiere().getIntitule() : null)
                .nombreQuestions(questionsDto.size())
                .questions(questionsDto)
                .build();
    }
}
