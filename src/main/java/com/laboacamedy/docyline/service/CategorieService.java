package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.CategorieRequest;
import com.laboacamedy.docyline.entities.Categorie;

import java.util.List;

/** Interface du service de gestion des categories. */
public interface CategorieService {
    Categorie creer(CategorieRequest requete);
    Categorie modifier(Long id, CategorieRequest requete);
    Categorie obtenirParId(Long id);
    List<Categorie> listerToutes();
    void supprimer(Long id);
}
