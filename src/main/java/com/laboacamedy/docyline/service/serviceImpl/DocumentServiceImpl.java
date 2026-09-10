package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.DocumentRequest;
import com.laboacamedy.docyline.dto.DocumentResponse;
import com.laboacamedy.docyline.entities.Categorie;
import com.laboacamedy.docyline.entities.Concours;
import com.laboacamedy.docyline.entities.Document;
import com.laboacamedy.docyline.entities.Matiere;
import com.laboacamedy.docyline.entities.enums.StatutDocument;
import com.laboacamedy.docyline.exception.RequeteInvalideException;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.CategorieRepository;
import com.laboacamedy.docyline.repository.ConcoursRepository;
import com.laboacamedy.docyline.repository.DocumentRepository;
import com.laboacamedy.docyline.repository.MatiereRepository;
import com.laboacamedy.docyline.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation du service de gestion des documents.
 * cf. besoins fonctionnels "Gestion des documents" et "Recherche, consultation et filtrage".
 */
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ConcoursRepository concoursRepository;
    private final MatiereRepository matiereRepository;
    private final CategorieRepository categorieRepository;

    @Value("${app.documents.storage-path}")
    private String storagePath;

    @Override
    @Transactional
    public DocumentResponse ajouter(DocumentRequest requete, MultipartFile fichierPdf, MultipartFile image) {
        if (fichierPdf == null || fichierPdf.isEmpty()){
            throw new RequeteInvalideException("Le fichier PDF est obligatoire");
        }

        String cheminPdf = enregistrerFichier(fichierPdf,"pdf");
        String cheminImage = (image != null && !image.isEmpty()) ? enregistrerFichier(image,"img"):null;

        Document document = Document.builder()
                .titre(requete.getTitre())
                .description(requete.getDescription())
                .prix(requete.getPrix())
                .annee(requete.getAnnee())
                .niveau(requete.getNiveau())
                .cheminFichierPdf(cheminPdf)
                .cheminImage(cheminImage)
                .statut(StatutDocument.DISPONIBLE)
                .nombreVentes(0)
                .concours(requete.getConcoursId() != null ? trouverConcours(requete.getConcoursId()) : null)
                .matiere(requete.getMatiereId() != null ? trouverMatiere(requete.getMatiereId()) : null)
                .categorie(requete.getCategorieId() != null ? trouverCategorie(requete.getCategorieId()) : null)
                .build();

        return versDto(documentRepository.save(document));
    }

    // Enregistre un fichier uploade sur le disque avec un nom unique (evite les collisions/ecrasements)
    private String enregistrerFichier(MultipartFile fichierPdf, String sousDossier) {
        try {
            Path dossier = Paths.get(storagePath, sousDossier);
            Files.createDirectories(dossier);

            String nomFichier = UUID.randomUUID() + "_" +fichierPdf.getOriginalFilename();
            Path cheminComplet = dossier.resolve(nomFichier);
            Files.copy(fichierPdf.getInputStream(), cheminComplet);

            return cheminComplet.toString();
        } catch (IOException e){
            throw  new RequeteInvalideException("Erreur lors de l'enregistrement du fichier: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public DocumentResponse modifier(Long id, DocumentRequest requete) {
        Document document = trouverDocument(id);
        document.setTitre(requete.getTitre());
        document.setDescription(requete.getDescription());
        document.setPrix(requete.getPrix());
        document.setAnnee(requete.getAnnee());
        document.setNiveau(requete.getNiveau());
        if (requete.getConcoursId() != null) document.setConcours(trouverConcours(requete.getConcoursId()));
        if (requete.getMatiereId() != null) document.setMatiere(trouverMatiere(requete.getMatiereId()));
        if (requete.getConcoursId() != null) document.setCategorie(trouverCategorie(requete.getCategorieId()));
        return versDto(documentRepository.save(document));
    }

    // --- Methodes utilitaires privees ---
    private Document trouverDocument(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(()-> new RessourceNonTrouveeException("Document introuvable avec l'id : " + id));
    }

    private Concours trouverConcours(Long id){
        return concoursRepository.findById(id)
                .orElseThrow(()-> new RessourceNonTrouveeException("Concours introuvable avec l'id: " + id));
    }

    private Matiere trouverMatiere(Long id){
        return matiereRepository.findById(id)
                .orElseThrow(()-> new RessourceNonTrouveeException("Matiere introuvable avec l'id : " + id));
    }

    private Categorie trouverCategorie(Long id){
        return categorieRepository.findById(id)
                .orElseThrow(()-> new RessourceNonTrouveeException("Categorie introuvable avec l'id : " + id));
    }

    @Override
    @Transactional
    public void changerStatut(Long id, boolean disponible) {
        Document document =  trouverDocument(id);
        document.setStatut(disponible ? StatutDocument.DISPONIBLE : StatutDocument.INDISPONIBLE);
        documentRepository.save(document);
    }

    @Override
    public DocumentResponse obtenirParId(Long id) {
        return versDto(trouverDocument(id));
    }

    private DocumentResponse versDto(Document d) {
        return DocumentResponse.builder()
                .id(d.getId())
                .titre(d.getTitre())
                .description(d.getDescription())
                .prix(d.getPrix())
                .cheminImage(d.getCheminImage())
                .annee(d.getAnnee())
                .statutDocument(d.getStatut())
                .nombreVentes(d.getNombreVentes())
                .nomConcours(d.getConcours() != null ? d.getConcours().getNom() : null)
                .nomMatiere(d.getMatiere() != null ? d.getMatiere().getIntitule() : null)
                .nomCategorie(d.getCategorie() != null ? d.getCategorie().getIntitule() : null)
                .build();
    }

    @Override
    public List<DocumentResponse> listerCatalogue() {
        return documentRepository.findByStatut(StatutDocument.DISPONIBLE)
                .stream().map(this::versDto).collect(Collectors.toList());
    }

    @Override
    public List<DocumentResponse> rechercher(String motCle) {
        return documentRepository.rechercherParMotCle(motCle)
                .stream().map(this::versDto).collect(Collectors.toList());
    }

    @Override
    public List<DocumentResponse> filtrerParConcours(Long concoursId) {
        return documentRepository.findByConcoursId(concoursId)
                .stream().map(this::versDto).collect(Collectors.toList());
    }

    @Override
    public List<DocumentResponse> filtrerParMatiere(Long matiereId) {
        return documentRepository.findByMatiereId(matiereId)
                .stream().map(this::versDto).collect(Collectors.toList());
    }

    @Override
    public List<DocumentResponse> filtrerParCategorie(Long categorieId) {
        return documentRepository.findByCategorieId(categorieId)
                .stream().map(this::versDto).collect(Collectors.toList());
    }

    @Override
    public List<DocumentResponse> documentsLesPlusVendus() {
        return documentRepository.findTop10ByOrderByNombreVentesDesc()
                .stream().map(this::versDto).collect(Collectors.toList());
    }
}
