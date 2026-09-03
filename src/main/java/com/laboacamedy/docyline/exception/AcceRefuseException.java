package com.laboacamedy.docyline.exception;


/** Exception levee lorsqu'un utilisateur tente d'acceder a une ressource non autorisee (-> HTTP 403). */
public class AcceRefuseException extends RuntimeException {
    public AcceRefuseException(String message) {
        super(message);
    }
}
