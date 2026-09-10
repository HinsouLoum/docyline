package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.CategorieRequest;
import com.laboacamedy.docyline.entities.Categorie;
import com.laboacamedy.docyline.exception.RequeteInvalideException;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.CategorieRepository;
import com.laboacamedy.docyline.service.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation du service de gestion des categories.
 * cf. besoin fonctionnel "Gestion des matieres et categories".
 */
@Service
@RequiredArgsConstructor
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;

    @Override
    public Categorie creer(CategorieRequest requete) {
        if (categorieRepository.existsByCode(requete.getCode())) {
            throw new RequeteInvalideException("Une categorie avec ce code existe deja");
        }
        Categorie categorie = Categorie.builder()
                .code(requete.getCode())
                .intitule(requete.getIntitule())
                .description(requete.getDescription())
                .build();
        return categorieRepository.save(categorie);
    }

    @Override
    public Categorie modifier(Long id, CategorieRequest requete) {
        Categorie categorie = obtenirParId(id);
        categorie.setIntitule(requete.getIntitule());
        categorie.setDescription(requete.getDescription());
        return categorieRepository.save(categorie);
    }

    @Override
    public Categorie obtenirParId(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Categorie introuvable avec l'id : " + id));
    }

    @Override
    public List<Categorie> listerToutes() {
        return categorieRepository.findAll();
    }

    @Override
    public void supprimer(Long id) {
        Categorie categorie = obtenirParId(id);
        categorieRepository.delete(categorie);
    }
}
