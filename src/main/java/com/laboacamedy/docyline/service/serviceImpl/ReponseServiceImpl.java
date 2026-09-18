package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.ReponseRequest;
import com.laboacamedy.docyline.dto.ReponseResponse;
import com.laboacamedy.docyline.entities.Question;
import com.laboacamedy.docyline.entities.Reponse;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.QuestionRepository;
import com.laboacamedy.docyline.repository.ReponseRepository;
import com.laboacamedy.docyline.service.ReponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/** Implementation du service de gestion des reponses. */
@Service
@RequiredArgsConstructor
@Transactional
public class ReponseServiceImpl implements ReponseService {

    private final ReponseRepository reponseRepository;
    private final QuestionRepository questionRepository;

    @Override
    public ReponseResponse creer(ReponseRequest requete) {
        Question question = questionRepository.findById(requete.getQuestionsId())
                .orElseThrow(() -> new RessourceNonTrouveeException("Question introuvable avec l'id : " + requete.getQuestionsId()));

        Reponse reponse = Reponse.builder()
                .contenu(requete.getContenu())
                .estCorrecte(requete.getEstCorrecte())
                .question(question)
                .build();

        Reponse reponseSauvegardee = reponseRepository.save(reponse);
        return mapperVersResponse(reponseSauvegardee);
    }

    @Override
    public ReponseResponse modifier(Long id, ReponseRequest requete) {
        Reponse reponse = reponseRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Reponse introuvable avec l'id : " + id));

        reponse.setContenu(requete.getContenu());
        reponse.setEstCorrecte(requete.getEstCorrecte());

        Reponse reponseModifiee = reponseRepository.save(reponse);
        return mapperVersResponse(reponseModifiee);
    }

    @Override
    @Transactional(readOnly = true)
    public ReponseResponse obtenirParId(Long id) {
        Reponse reponse = reponseRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Reponse introuvable avec l'id : " + id));
        return mapperVersResponse(reponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReponseResponse> listerParQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new RessourceNonTrouveeException("Question introuvable avec l'id : " + questionId);
        }
        return reponseRepository.findAll().stream()
                .filter(r -> r.getQuestion().getId().equals(questionId))
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReponseResponse> listerToutes() {
        return reponseRepository.findAll().stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void supprimer(Long id) {
        Reponse reponse = reponseRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Reponse introuvable avec l'id : " + id));
        reponseRepository.delete(reponse);
    }

    private ReponseResponse mapperVersResponse(Reponse reponse) {
        return ReponseResponse.builder()
                .id(reponse.getId())
                .contenu(reponse.getContenu())
                .estCorrecte(reponse.getEstCorrecte())
                .questionId(reponse.getQuestion().getId())
                .build();
    }
}
