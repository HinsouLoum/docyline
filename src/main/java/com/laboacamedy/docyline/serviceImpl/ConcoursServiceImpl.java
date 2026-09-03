package com.laboacamedy.docyline.serviceImpl;

import com.laboacamedy.docyline.dto.ConcoursRequest;
import com.laboacamedy.docyline.entities.Concours;
import com.laboacamedy.docyline.entities.Matiere;
import com.laboacamedy.docyline.enums.StatutConcours;
import com.laboacamedy.docyline.exception.RequeteInvalideException;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.ConcoursRepository;
import com.laboacamedy.docyline.repository.MatiereRepository;
import com.laboacamedy.docyline.service.ConcoursService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation du service de gestion des concours.
 * cf. besoin fonctionnel "Gestion des concours".
 */
@Service
@RequiredArgsConstructor
public class ConcoursServiceImpl implements ConcoursService {

    private final ConcoursRepository concoursRepository;
    private final MatiereRepository matiereRepository;

    @Override
    public Concours creer(ConcoursRequest requete) {
        if (concoursRepository.existsByCode(requete.getCode())){
            throw new RequeteInvalideException("Un concours avec ce code existe deja");
        }

        Concours concours = Concours.builder()
                .code(requete.getCode())
                .nom(requete.getNom())
                .description(requete.getDescription())
                .organisme(requete.getOrganisme())
                .annee(requete.getAnnee())
                .niveau(requete.getNiveau())
                .conditionsModalites(requete.getConditionsMolites())
                .statut(StatutConcours.ACTIF)
                .build();

        associerMatieres(concours,requete.getMatiereIds());
        return concoursRepository.save(concours);
    }

    @Override
    @Transactional
    public Concours modifier(Long id, ConcoursRequest requete) {
        Concours concours = obtenirParId(id);
        concours.setNom(requete.getNom());
        concours.setDescription(requete.getDescription());
        concours.setOrganisme(requete.getOrganisme());
        concours.setNiveau(requete.getNiveau());
        concours.setConditionsModalites(requete.getConditionsMolites());
        associerMatieres(concours, requete.getMatiereIds());
        return concoursRepository.save(concours);
    }

    @Override
    public void changerStatutConcour(Long id, boolean actif) {
        Concours concours = obtenirParId(id);
        concours.setStatut(actif ? StatutConcours.ACTIF : StatutConcours.INACTIF);
        concoursRepository.save(concours);

    }

    @Override
    public Concours obtenirParId(Long id) {
        return concoursRepository.findById(id)
                .orElseThrow(()-> new RessourceNonTrouveeException("Concours introuvable avec l'id : " + id));
    }

    @Override
    public List<Concours> listerTous() {
        return concoursRepository.findAll();
    }

    @Override
    public List<Concours> listerActifs() {
        return concoursRepository.findByStatut(StatutConcours.ACTIF);
    }

//    Associe les matieres selectionnees au concours (relation many-to-many)
    private void associerMatieres(Concours concours,List<Long> matiereIds){
        if (matiereIds == null) return;;
        List<Matiere> matieres = matiereRepository.findAllById(matiereIds);
        concours.setMatieres(matieres);
    }
}
