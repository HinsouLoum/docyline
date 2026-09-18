package com.laboacamedy.docyline.entities.enums;

/** Enumeration des types de consultation de document. */
public enum TypeConsultation {
    VISUALISATION("Visualisation"),
    TELECHARGEMENT("Téléchargement"),
    PARTAGE("Partage");

    private final String libelle;

    TypeConsultation(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}