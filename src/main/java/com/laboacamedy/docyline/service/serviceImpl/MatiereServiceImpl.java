package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.MatiereRequest;
import com.laboacamedy.docyline.entities.Matiere;
import com.laboacamedy.docyline.exception.RequeteInvalideException;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.MatiereRepository;
import com.laboacamedy.docyline.service.MatiereService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation du service de gestion des matieres.
 * cf. besoin fonctionnel "Gestion des matieres et categories".
 */
@Service
@RequiredArgsConstructor
public class MatiereServiceImpl implements MatiereService {

    private final MatiereRepository matiereRepository;

    @Override
    public Matiere creer(MatiereRequest requete) {
        if (matiereRepository.existsByCode(requete.getCode())) {
            throw new RequeteInvalideException("Une matiere avec ce code existe deja");
        }
        Matiere matiere = Matiere.builder()
                .code(requete.getCode())
                .intitule(requete.getIntitule())
                .description(requete.getDescription())
                .build();
        return matiereRepository.save(matiere);
    }

    @Override
    public Matiere modifier(Long id, MatiereRequest requete) {
        Matiere matiere = obtenirParId(id);
        matiere.setIntitule(requete.getIntitule());
        matiere.setDescription(requete.getDescription());
        return matiereRepository.save(matiere);
    }

    @Override
    public Matiere obtenirParId(Long id) {
        return matiereRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Matiere introuvable avec l'id : " + id));
    }

    @Override
    public List<Matiere> listerToutes() {
        return matiereRepository.findAll();
    }

    @Override
    public void supprimer(Long id) {
        Matiere matiere = obtenirParId(id);
        matiereRepository.delete(matiere);
    }
}
